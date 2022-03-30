package disjointsets;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A quick-union-by-size data structure with path compression.
 *
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    private int size;
    private HashMap<T, Integer> index;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<Integer>();
        size = 0;
        index = new HashMap<T, Integer>();
    }

    @Override
    public void makeSet(T item) {
        pointers.add(-1);
        index.put(item, size);
        size++;
    }

    @Override
    public int findSet(T item) {
        Integer nextIndex = index.get(item);
        if (nextIndex == null) {
            throw new IllegalArgumentException(item + " is not in any set.");
        }
        Set<Integer> visited = new HashSet<Integer>();
        while (pointers.get(nextIndex) >= 0) {
            visited.add(nextIndex);
            nextIndex = pointers.get(nextIndex);
        }
        for (int visitedIndex : visited) {
            pointers.set(visitedIndex, nextIndex);
        }
        return nextIndex.intValue();
    }

    @Override
    public boolean union(T item1, T item2) {
        int first = findSet(item1);
        int second = findSet(item2);
        if (first == second) {
            return false;
        }
        int weight1 = pointers.get(first);
        int weight2 = pointers.get(second);
        if (weight1 <= weight2) {
            pointers.set(second, first);
            pointers.set(first, weight1 + weight2);
        } else {
            pointers.set(first, second);
            pointers.set(second, weight1 + weight2);
        }
        return true;
    }
}
