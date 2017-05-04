package com.softkiwi.algorithms.graphs;

class Edge {

	private Object from, to;

	static Edge create(Object from, Object to) {

		Edge e = new Edge();
		e.from = from;
		e.to = to;

		return e;

	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Edge edge = (Edge) o;

		if (from != null ? !from.equals(edge.from) : edge.from != null)
			return false;
		return to != null ? to.equals(edge.to) : edge.to == null;

	}

	@Override
	public int hashCode() {
		int result = from != null ? from.hashCode() : 0;
		result = 31 * result + (to != null ? to.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return from + " -> " + to;
	}
}
