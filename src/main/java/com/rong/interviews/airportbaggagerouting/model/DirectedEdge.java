package com.rong.interviews.airportbaggagerouting.model;

/**
 * Created by rongyj on 3/18/17.
 */
public class DirectedEdge {
    private final Vertex source;
    private final Vertex destination;
    private final int time;

    public DirectedEdge(Vertex source, Vertex destination, int time) {
        this.source = source;
        this.destination = destination;
        this.time = time;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public int getTime() {
        return time;
    }
}
