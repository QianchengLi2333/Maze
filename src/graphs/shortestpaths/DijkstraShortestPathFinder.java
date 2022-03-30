package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, E> spt = new HashMap<>();
        if (start.equals(end)) {
            return spt;
        }
        Map<V, Double> distanceTo = new HashMap<>();
        DoubleMapMinPQ<V> vertices = new DoubleMapMinPQ<V>();
        vertices.add(start, 0);
        distanceTo.put(start, 0.0);
        //spt.put(start, null);
        while (!vertices.peekMin().equals(end)) {
            V startPoint = vertices.removeMin();
            for (E edge : graph.outgoingEdgesFrom(startPoint)) {
                if (!spt.containsKey(edge.to()) && !edge.to().equals(start)) {
                    spt.put(edge.to(), edge);
                    distanceTo.put(edge.to(), distanceTo.get(edge.from()) + edge.weight());
                    vertices.add(edge.to(), distanceTo.get(edge.to()));
                } else {
                    if (distanceTo.get(edge.from()) + edge.weight() < distanceTo.get(edge.to())) {
                        spt.put(edge.to(), edge);
                        distanceTo.put(edge.to(), distanceTo.get(edge.from()) + edge.weight());
                        if (vertices.contains(edge.to())) {
                            vertices.changePriority(edge.to(), distanceTo.get(edge.to()));
                        }
                    }
                }
            }
            if (vertices.isEmpty()) {
                return spt;
            }
        }
        return spt;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (start.equals(end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        E endEdge = spt.get(end);
        if (endEdge == null) {
            return new ShortestPath.Failure<>();
        }
        DoubleMapMinPQ<V> vertices = new DoubleMapMinPQ<V>();
        List<E> path = new ArrayList<E>();
        path.add(endEdge);
        V startVertex = endEdge.from();
        if (startVertex.equals(start)) {
            return new ShortestPath.Success<>(path);
        }
        vertices.add(startVertex, 0);
        while (!vertices.isEmpty()) {
            V vertex1 = vertices.removeMin();
            E startEdge = spt.get(vertex1);
            if (startEdge != null) {
                path.add(startEdge);
                V vertex2 = startEdge.from();
                vertices.add(vertex2, 0);
            }
        }
        List<E> result = new ArrayList<E>();
        for (int i = path.size() - 1; i >= 0; i--) {
            result.add(path.get(i));
        }
        return new ShortestPath.Success<>(result);
    }
}
