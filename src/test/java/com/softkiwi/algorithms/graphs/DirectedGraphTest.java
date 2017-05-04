package com.softkiwi.algorithms.graphs;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by kay on 12/12/2016.
 */

public class DirectedGraphTest {

	// create nodes
	private Node<String> a = new Node<String>("a");
	private Node<String> b = new Node<String>("b");
	private Node<String> c = new Node<String>("c");
	private Node<String> d = new Node<String>("d");
	private Node<String> e = new Node<String>("e");
	private Node<String> f = new Node<String>("f");
	private Node<String> x = new Node<String>("x");
	private Node<String> y = new Node<String>("y");

	@Test
	public void test_SortShouldSortNodesNormally() {
		DefaultGraph<String> graph = new DefaultGraph<String>();

		// add to graph
		graph.addVertex(a, b, c, d, e, f, x, y);

		// connect nodes
		graph.addEdge(b, a);
		graph.addEdge(d, a);
		graph.addEdge(c, b);
		graph.addEdge(b, x);
		graph.addEdge(e, f);

		// sort nodes
		assetSortString(graph, "a, x, b, c, d, f, e, y, ");
	}

	@Test
	public void test_SortShouldDetectCyclicGraph() {

		DefaultGraph<String> graph = new DefaultGraph<String>();

		// add to graph
		graph.addVertex(a, b, d, x, e, y);

		// connect nodes
		graph.addEdge(b, a);
		graph.addEdge(d, a);
		graph.addEdge(c, b);
		graph.addEdge(b, x);
		graph.addEdge(e, f);

		// add cycle
		graph.addEdge(x, e);
		graph.addEdge(e, d);
		graph.addEdge(d, b);
		graph.addEdge(y, x);

		// sort nodes
		assetThrowCycleException(graph);

	}

	@Test
	public void test_ShouldSortTransitiveDependencies() {
		DefaultGraph<String> graph = new DefaultGraph<String>();

		// Hierarchy Dependency
		graph.addVertex(a, b, c, d);

		graph.addEdge(c, a);
		graph.addEdge(c, b);
		graph.addEdge(c, b);
		graph.addEdge(d, c);
		graph.addEdge(d, a);
		graph.addEdge(d, b);

		// sort nodes
		assetSortString(graph, "a, b, c, d, ");
	}

	@Test
	public void test_ShouldDetectFullCycle() {
		DefaultGraph<String> graph = new DefaultGraph<String>();

		// Hierarchy Dependency
		graph.addVertex(a, b, c, d);

		graph.addEdge(a, b);
		graph.addEdge(b, c);
		graph.addEdge(c, d);
		graph.addEdge(d, a);

		// sort nodes
		assetThrowCycleException(graph);
	}

	@Test
	public void test_ShouldSortIndependentNodes() {
		DefaultGraph<String> graph = new DefaultGraph<String>();

		// Hierarchy Dependency
		graph.addVertex(a, b, c, d);

		// sort nodes
		assetSortString(graph, "a, b, c, d, ");

	}

	private void assetSortString(DefaultGraph<String> graph, String expected) {
		graph.sort();
		assertEquals(expected, graph.sortString());
	}

	private void assetThrowCycleException(DefaultGraph<String> graph) {
		try {
			graph.sort();
			fail("Should have throw cyclic exception");
		} catch (Exception ex) {
			assertTrue("Should have throw cyclic exception", ex.getMessage().contains("cyclic"));
		}
	}
}