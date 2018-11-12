package com.softkiwi.algorithms.graphs;

import java.util.ArrayList;
import java.util.List;

public class DefaultDirectedGraph<DATA> extends AbstractDirectedGraph<DATA, Node<DATA>> {

    public void addVertex(DATA... vertex) {

        for (DATA data : vertex) {
            addVertex(new Node<DATA>(data));
        }

    }

    public List<DATA> getSortedVertexIds() {
        List<Node<DATA>> sort = sort();

        List<DATA> data = new ArrayList<DATA>(sort.size());

        for (Node<DATA> item : sort) {
            data.add(item.getVertexId());
        }

        return data;
    }
}