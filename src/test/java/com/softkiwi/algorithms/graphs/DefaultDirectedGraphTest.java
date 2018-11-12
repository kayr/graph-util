package com.softkiwi.algorithms.graphs;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by kay on 12/12/2016.
 */

public class DefaultDirectedGraphTest {

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
        DefaultDirectedGraph<String> graph = new DefaultDirectedGraph<String>();

		// add to graph
		graph.addVertex(a, b, c, d, e, f, x, y);

		// connect nodes
		graph.addEdge(b, a);
		graph.addEdge(d, a);
		graph.addEdge(c, b);
		graph.addEdge(b, x);
		graph.addEdge(e, f);

		// sort nodes
        assetSortString(graph, a, x, b, c, d, f, e, y);
	}

	@Test
	public void test_SortShouldDetectCyclicGraph() {

        DefaultDirectedGraph<String> graph = new DefaultDirectedGraph<String>();

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
        List<Node<String>> nodes = graph.sort(false);


        System.out.println(nodes);

        assertEquals(8, nodes.size());


    }

    @Test
    public void test_sampleOfficeHierachy() {
        DefaultDirectedGraph<Integer> graph = new DefaultDirectedGraph<Integer>();

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
        DefaultDirectedGraph<String> graph = new DefaultDirectedGraph<String>();

		// Hierarchy Dependency
		graph.addVertex(a, b, c, d);

		graph.addEdge(c, a);
		graph.addEdge(c, b);
		graph.addEdge(c, b);
		graph.addEdge(d, c);
		graph.addEdge(d, a);
		graph.addEdge(d, b);

		// sort nodes
        assetSortString(graph, a, b, c, d);
	}

	@Test
	public void test_ShouldDetectFullCycle() {
        DefaultDirectedGraph<String> graph = new DefaultDirectedGraph<String>();

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
        DefaultDirectedGraph<String> graph = new DefaultDirectedGraph<String>();

		// Hierarchy Dependency
		graph.addVertex(a, b, c, d);

		// sort nodes
        assetSortString(graph, a, b, c, d);

	}

	@Test
	public void test_getParentFromCycle(){

        DefaultDirectedGraph<Integer> graph = new DefaultDirectedGraph<Integer>();

        graph.addVertex(n(1), n(2), n(3), n(4), n(5));

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
		graph.addEdge(2,4);
		graph.addEdge(2,5);
		graph.addEdge(3,5);
		graph.addEdge(4,3);

		Set<Integer> parent = graph.getParent(5);

        graph.sort();

		assertArrayEquals(new Integer[]{2,1,3,4},parent.toArray(new Integer[0]));
	}

    @Test
    public void test_Sort2() {

        DefaultDirectedGraph<Integer> graph = new DefaultDirectedGraph<Integer>();

        Node<Integer> _1 = n(1);
        Node<Integer> _2 = n(2);
        Node<Integer> _3 = n(3);
        Node<Integer> _4 = n(4);
        Node<Integer> _5 = n(5);
        Node<Integer> _0 = n(0);
        graph.addVertex(_1, _2, _3, _4, _5, _0);

        graph.addEdge(5, 2);
        graph.addEdge(5, 0);
        graph.addEdge(4, 0);
        graph.addEdge(4, 1);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);

        System.out.println(graph.toString());

        assetSortString(graph, _0, _1, _4, _3, _2, _5);

    }

    private void assetSortString(DefaultDirectedGraph graph, Object... items) {
        List sort = graph.sort();
        assertArrayEquals(sort.toArray(), items);
    }

    private void assetThrowCycleException(DefaultDirectedGraph<String> graph) {
		try {
			graph.sort();
			fail("Should have throw cyclic exception");
		} catch (Exception ex) {
            System.out.println(ex.getMessage());
			assertTrue("Should have throw cyclic exception", ex.getMessage().contains("cyclic"));
		}
	}
}