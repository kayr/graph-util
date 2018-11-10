package com.softkiwi.algorithms.graphs;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Queue;
import java.util.Set;

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
        graph.addVertex(a, b, d, c, x, e, y, f);

		// connect nodes
		graph.addEdge(b, a);
		graph.addEdge(d, a);
		graph.addEdge(c, b);
		graph.addEdge(b, x);
		graph.addEdge(e, f);

		// add cycle
		graph.addEdge(x, e);
		graph.addEdge(e, d);
		graph.addEdge(y, x);
        graph.addEdge(a, y);


		// sort nodes
		assetThrowCycleException(graph);

        //sort silently
        Queue<Node<String>> nodes = graph.sort(false);


        System.out.println(nodes);

        assertEquals(8, nodes.size());


    }

    @Test
    public void test_sampleOfficeHierachy() {
        DefaultGraph<Integer> graph = new DefaultGraph<Integer>();

        graph.addVertex(n(1), n(2), n(3), n(4), n(7), n(8), n(3), n(4), n(10), n(9));

        graph.addEdge(1, 2).addEdge(2, 3).addEdge(2, 7)
                .addEdge(2, 8).addEdge(8, 9).addEdge(9, 10)
                .addEdge(1, 3).addEdge(3, 4)
        .addEdge(1,1);

        for (Integer v : graph.getVertices()) {
            Set<Integer> parent = graph.getParent(v);
            System.out.println(v + " : " + parent);
        }

    }

    Node<Integer> n(int num) {
        return new Node<Integer>(num);
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

	@Test
	public void test_getParentFromCycle(){

		DefaultGraph<Integer> graph = new DefaultGraph<Integer>();

		graph.addVertex(new Node<Integer>(1),
				new Node<Integer>(2),
				new Node<Integer>(3),
				new Node<Integer>(4),
				new Node<Integer>(5));

		graph.addEdge(1,2);
		graph.addEdge(1,3);
		graph.addEdge(2,4);
		graph.addEdge(2,5);
		graph.addEdge(3,5);
		graph.addEdge(4,3);

		Set<Integer> parent = graph.getParent(5);

		assertArrayEquals(new Integer[]{2,1,3,4},parent.toArray(new Integer[0]));
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
            System.out.println(ex.getMessage());
			assertTrue("Should have throw cyclic exception", ex.getMessage().contains("cyclic"));
		}
	}
}