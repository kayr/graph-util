package com.softkiwi.algorithms.graphs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class AbstractDirectedGraph<K, T extends VertexData<K>> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDirectedGraph.class);

    private Map<K, T> vertices = new LinkedHashMap<K, T>(); // vertex mapper

	private Map<K, Set<K>> outgoingMapList = new LinkedHashMap<K, Set<K>>(); // adjacencyList connections

	private Map<K, Set<K>> incomingMapList = new LinkedHashMap<K, Set<K>>(); // invertedAdjacencyList connections

    private Map<K, Boolean> visited = new LinkedHashMap<K, Boolean>(); // visited nodes

	private List<T> result = new ArrayList<T>(); // result for sorted nodes

    public void addVertex(T... vertex) {
		for (T v : vertex) {
            K id = v.getVertexId();

			if (!vertices.containsKey(id)) {
				vertices.put(id, v);
				outgoingMapList.put(id, new LinkedHashSet<K>());
				incomingMapList.put(id, new LinkedHashSet<K>());
			}
		}
	}

    public Set<K> getVertices() {
        return vertices.keySet();
    }

	public AbstractDirectedGraph<K, T> addEdge(K from, K to) {

		if (!outgoingMapList.containsKey(from)) throw new GraphException("Vertex not found[" + from + "]");


		if (!outgoingMapList.containsKey(to)) throw new GraphException("Vertex not found [" + to + "]");

		Set<K> outgoingList = this.outgoingMapList.get(from);
		Set<K> incomingList = this.incomingMapList.get(to);


        outgoingList.add(to);
        incomingList.add(from);

        return this;
    }

	public void addEdge(T from, T to) {
		addEdge(from.getVertexId(), to.getVertexId());
	}


    public Set<K> getParent(K key) {

        Set<K> parentKeys = new LinkedHashSet<K>();

		Set<K> parents = incomingMapList.get(key);

        Deque<K> deque = new ArrayDeque<K>(parents);

        K topKey = deque.poll();
        while (topKey != null) {
            if (parentKeys.contains(topKey)) {
                topKey = deque.poll();
			} else {
                parentKeys.add(topKey);
				Set<K> parentsOfParent = incomingMapList.get(topKey);
                for (K p : parentsOfParent) {
					deque.addFirst(p);//add to the top so that we visit the follow this path next
				}
                topKey = deque.poll();
			}

		}

        return parentKeys;
	}

	public List<T> sort() {
        return sort(true);
    }

	public List<T> sort(boolean failOnCycle) {
		visited.clear();
		result.clear();

		// nodes with no incoming edges
		for (Map.Entry<K, Set<K>> n : incomingMapList.entrySet()) {
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

			for (K _toId : outgoingMapList.get(fromId)) {
                Set<K> visitedChildren = fromRoot ? new HashSet<K>(Collections.singletonList(fromId)) : visited;
				visit(_toId, fromId, visitedChildren, false, failOnCycle);
			}

			markAsVisited(fromId);

			result.add(vertices.get(fromId));
		}
	}


	public Map<K, Set<K>> getOutgoingMapList() {
		return outgoingMapList;
	}

	public Map<K, Set<K>> getIncomingMapList() {
		return incomingMapList;
	}


}