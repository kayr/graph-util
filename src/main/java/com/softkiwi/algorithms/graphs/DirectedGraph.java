package com.softkiwi.algorithms.graphs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class DirectedGraph<K, T extends VertexData<K>> {

    private static final Logger LOG = LoggerFactory.getLogger(DirectedGraph.class);

    private Map<K, T> vertices = new LinkedHashMap<K, T>(); // vertex mapper

    private Map<K, ArrayList<K>> adjacencyList = new LinkedHashMap<K, ArrayList<K>>(); // outgoing connections

    private Map<K, ArrayList<K>> invertedAdjacencyList = new LinkedHashMap<K, ArrayList<K>>(); // incoming connections

    private Map<K, Boolean> visited = new LinkedHashMap<K, Boolean>(); // visited nodes

	private Queue<T> result = new LinkedList<T>(); // result for sorted nodes

    public void addVertex(T... vertex) {
		for (T v : vertex) {
            K id = v.getVertexId();

			if (!vertices.containsKey(id)) {
				vertices.put(id, v);
                adjacencyList.put(id, new ArrayList<K>());
                invertedAdjacencyList.put(id, new ArrayList<K>());
			}
		}
	}

    public Set<K> getVertices() {
        return vertices.keySet();
    }

    public DirectedGraph<K, T> addEdge(K from, K to) {

        if (!adjacencyList.containsKey(from)) throw new GraphException("Vertex not found[" + from + "]");


        if (!adjacencyList.containsKey(to)) throw new GraphException("Vertex not found [" + to + "]");

        ArrayList<K> outgoingList = adjacencyList.get(from);
        ArrayList<K> incomingList = invertedAdjacencyList.get(to);

        if (outgoingList.contains(to) || incomingList.contains(from)) {
            return this;
        }

        outgoingList.add(to);
        incomingList.add(from);

        return this;
    }

	public void addEdge(T from, T to) {
		addEdge(from.getVertexId(), to.getVertexId());
	}


    public Set<K> getParent(K key) {

        Set<K> parentKeys = new LinkedHashSet<K>();

        ArrayList<K> parents = invertedAdjacencyList.get(key);

        Deque<K> deque = new ArrayDeque<K>(parents);

        K topKey = deque.poll();
        while (topKey != null) {
            if (parentKeys.contains(topKey)) {
                topKey = deque.poll();
			} else {
                parentKeys.add(topKey);
                ArrayList<K> parentsOfParent = invertedAdjacencyList.get(topKey);
                for (K p : parentsOfParent) {
					deque.addFirst(p);//add to the top so that we visit the follow this path next
				}
                topKey = deque.poll();
			}

		}

        return parentKeys;
	}

    public Queue<T> sort() {
        return sort(true);
    }

    public Queue<T> sort(boolean failOnCycle) {
		visited.clear();
		result.clear();

		// nodes with no incoming edges
        for (Map.Entry<K, ArrayList<K>> n : invertedAdjacencyList.entrySet()) {
            if (n.getValue().isEmpty()) {
                visit(n.getKey(), null, new HashSet<K>(), true, failOnCycle);
			}
		}

		if (failOnCycle && result.size() != vertices.size()) {// We may have cycles.. i.e no independent node.. a -> b -> c -> a
            throw new GraphException(
					"Could Not Sort Graph..Check to make sure you do not have any cyclic dependencies");
		}

		return result;
	}


    private void markAsVisited(K id) {
		visited.put(id, true);
	}

    private boolean isVisited(K id) {
		if (visited.containsKey(id))
			return visited.get(id);

		return false;
	}

    private void visit(K fromId, K toId, Set<K> visited, boolean fromRoot, boolean failOnCycle) {
		if (!isVisited(fromId)) {

			LOG.info("Visiting [from:{} to:{}] Parents: {}",toId,fromId,visited);

			if (visited.contains(fromId)) {
				if (failOnCycle)
                    throw new GraphException(
							"cyclic graph found moving from [" + fromId + "->" + toId + "] parents = " + visited);

				return;

			}

			visited.add(fromId);

            for (K _toId : adjacencyList.get(fromId)) {
                Set<K> visitedChildren = fromRoot ? new HashSet<K>(Collections.singletonList(fromId)) : visited;
				visit(_toId, fromId, visitedChildren, false, failOnCycle);
			}

			markAsVisited(fromId);

			result.offer(vertices.get(fromId));
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("adjacencyList:");
		sb.append("\n");
        for (Map.Entry<K, ArrayList<K>> v : adjacencyList.entrySet()) {
            StringBuilder x = new StringBuilder().append(v.getKey()).append(" -> [");
            for (K c : v.getValue()) {
                x.append(c).append(", ");
			}
            x.append("]");
			sb.append(x);
			sb.append("\n");
		}

		sb.append("\n");
		sb.append("\n");

		sb.append("invertedAdjacencyList:");
		sb.append("\n");
        for (Map.Entry<K, ArrayList<K>> v : invertedAdjacencyList.entrySet()) {
            StringBuilder x = new StringBuilder().append(v.getKey()).append(" <- [");
            for (K c : v.getValue()) {
                x.append(c).append(", ");
			}
            x.append("]");
			sb.append(x);
			sb.append("\n");
		}
		sb.append("\n");
		sb.append("Sorted:\n");
		for (T head : result) {
			sb.append(head.getVertexId()).append(", ");
		}
		sb.append("\n");

		return sb.toString();
	}

	public String sortString() {
		StringBuilder sb = new StringBuilder();
		for (T head : result) {
			sb.append(head.getVertexId()).append(", ");
		}
		return sb.toString();
	}
}