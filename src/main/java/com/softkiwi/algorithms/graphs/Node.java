package com.softkiwi.algorithms.graphs;

public class Node<DATA> implements VertexData<DATA> {

	private DATA data;

	public Node(DATA label) {
        this.data = label;
	}

	@Override
	public DATA getVertexId() {
		return data;
	}

	@Override
	public String toString() {
		return "Node(" + data + ")";
	}
}


