package mazes.logic.carvers;

import edu.princeton.cs.algs4.SET;
import graphs.EdgeWithData;
import graphs.Graph;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        // Hint: you'll probably need to include something like the following:
        // this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges));
        Collection<EdgeWithData<Room, Wall>> edges = new HashSet<EdgeWithData<Room, Wall>>();
        for (Wall wall : walls) {
            EdgeWithData<Room, Wall> edge = new EdgeWithData<>(wall.getRoom1(), wall.getRoom2(), rand.nextDouble(), wall);
            edges.add(edge);
        }
        Collection<EdgeWithData<Room, Wall>> mst = this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges)).edges();
        Set<Wall> result = new HashSet<Wall>();
        for (EdgeWithData<Room, Wall> edge : mst) {
            Wall wall = edge.data();
            result.add(wall);
        }
        return result;
    }
}
