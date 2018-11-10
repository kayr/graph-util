package com.softkiwi.algorithms.graphs;

public class GraphException extends RuntimeException {

    public GraphException() {
    }

    public GraphException(String message) {
        super(message);
    }

    public GraphException(String message, Throwable cause) {
        super(message, cause);
    }
}
