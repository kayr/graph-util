package com.softkiwi.algorithms.graphs;

import java.util.*;

public abstract class DirectedGraph<KEY, T extends VertexData<KEY>> {

	private Map<KEY, T> vertices = new LinkedHashMap<KEY, T>(); // vertex mapper

	private Map<KEY, ArrayList<KEY>> adjacencyList = new LinkedHashMap<KEY, ArrayList<KEY>>(); // outgoing connections

	private Map<KEY, ArrayList<KEY>> invertedAdjacencyList = new LinkedHashMap<KEY, ArrayList<KEY>>(); // incoming connections

	private Map<KEY, Boolean> visited = new LinkedHashMap<KEY, Boolean>(); // visited nodes

	private Queue<T> result = new LinkedList<T>(); // result for sorted nodes
	private Map<KEY, List<KEY>> parentList = new LinkedHashMap<KEY, List<KEY>>();

	public void addVertex(T... vertex) {
		for (T v : vertex) {
			KEY id = v.getVertexId();

			if (!vertices.containsKey(id)) {
				vertices.put(id, v);
				adjacencyList.put(id, new ArrayList<KEY>());
				invertedAdjacencyList.put(id, new ArrayList<KEY>());
			}
		}
	}

	public void addEdge(KEY from, KEY to) {
		if (adjacencyList.containsKey(from) && adjacencyList.containsKey(to)) {
			ArrayList<KEY> outgoingList = adjacencyList.get(from);
			ArrayList<KEY> incomingList = invertedAdjacencyList.get(to);

			if (outgoingList.contains(to) || incomingList.contains(from)) {
				return;
			}

			outgoingList.add(to);
			incomingList.add(from);
		}
	}

	public void addEdge(T from, T to) {
		addEdge(from.getVertexId(), to.getVertexId());
	}


	public Set<KEY> getParent(KEY key) {

		Set<KEY> result = new LinkedHashSet<KEY>();

		ArrayList<KEY> parents = invertedAdjacencyList.get(key);

		Deque<KEY> deque = new ArrayDeque<KEY>(parents);

		KEY pop = deque.poll();
		while (pop != null) {
			if (result.contains(pop)) {
				pop = deque.poll();
			} else {
				result.add(pop);
				ArrayList<KEY> parentsOfParent = invertedAdjacencyList.get(pop);
				for(KEY p :parentsOfParent) {
					deque.addFirst(p);//add to the top so that we visit the follow this path next
				}
				pop = deque.poll();
			}

		}

		return result;
	}

    public Queue<T> sort() {
        return sort(true);
    }

    public Queue<T> sort(boolean failOnCycle) {
		visited.clear();
		result.clear();

		// nodes with no incoming edges
		for (KEY n : invertedAdjacencyList.keySet()) {
			if (invertedAdjacencyList.get(n).isEmpty()) {
                visit(n, null, new HashSet<KEY>(), true, failOnCycle);


			}
		}

		if (failOnCycle && result.size() != vertices.size()) {// We may have cycles.. i.e no independent node.. a -> b -> c -> a
			throw new RuntimeException(
					"Could Not Sort Graph..Check to make sure you do not have any cyclic dependencies");
		}

		return result;
	}



	private void markAsVisited(KEY id) {
		visited.put(id, true);
	}

	private boolean isVisited(KEY id) {
		if (visited.containsKey(id))
			return visited.get(id);

		return false;
	}

	private void visit(KEY fromId, KEY toId, Set<KEY> visited, boolean fromRoot, boolean failOnCycle) {
		if (!isVisited(fromId)) {

			System.out.println("Visiting [from:" + toId + " to:" + fromId + "] Parents: " + visited);

			if (visited.contains(fromId)) {
				if (failOnCycle)
					throw new RuntimeException(
							"cyclic graph found moving from [" + fromId + "->" + toId + "] parents = " + visited);

				return;

			}

			visited.add(fromId);

			for (KEY _toId : adjacencyList.get(fromId)) {
				Set<KEY> visitedChildren = fromRoot ? new HashSet<KEY>(Collections.singletonList(fromId)) : visited;
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
		for (KEY v : adjacencyList.keySet()) {
			String x = v + " -> [";
			for (KEY c : adjacencyList.get(v)) {
				x += c + ", ";
			}
			x += "]";
			sb.append(x);
			sb.append("\n");
		}

		sb.append("\n");
		sb.append("\n");

		sb.append("invertedAdjacencyList:");
		sb.append("\n");
		for (KEY v : invertedAdjacencyList.keySet()) {
			String x = v + " <- [";
			for (KEY c : invertedAdjacencyList.get(v)) {
				x += c + ", ";
			}
			x += "]";
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