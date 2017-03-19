package com.rong.interviews.airportbaggagerouting.model;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Created by rongyj on 3/18/17.
 */
public class DijkstraGraphMap {
    // mapping of vertex names to Vertex objects, built from a set of Edges
    private final Map<String, Vertex> graphMap;
    public DijkstraGraphMap(List<DirectedEdge> directedEdges) {

        graphMap = new HashMap<>(directedEdges.size());

        //Populated all the vertices from the edges
        for (DirectedEdge e : directedEdges) {
            if (!graphMap.containsKey(e.getSource().getName())) graphMap.put(e.getSource().getName(), new Vertex(e.getSource().getName()));
            if (!graphMap.containsKey(e.getDestination().getName())) graphMap.put(e.getDestination().getName(), new Vertex(e.getDestination().getName()));
        }

        //Set all the neighbours
        for (DirectedEdge e : directedEdges) {
            graphMap.get(e.getSource().getName()).getNeighbours().put(graphMap.get(e.getDestination().getName()), e.getTime());
        }
    }


    /**
     * Runs dijkstra algorithm using a specified source vertex.
     * Every time when the starting vertex get changed, this algorithm should get run.
     * @param startName the starting or source Vertex for the path
     */
    public void dijkstra(String startName) {
        if (!graphMap.containsKey(startName)) {
            throw new DijkstraGraphMapException("This DijkstraGraphMap does not contain the starting Vertex named:"+startName);
        }
        final Vertex source = graphMap.get(startName);
        NavigableSet<Vertex> queue = new TreeSet<>();

        // populate vertices to the queue
        for (Vertex v : graphMap.values()) {
            v.setPrevVertext( v == source ? source : null);
            v.setTime(v == source ? 0 : Integer.MAX_VALUE);
            queue.add(v);
        }

        dijkstra(queue);
    }

    /**
     * Get the shortest path as a list of Vertex for a specific destination Vertex with name as endName
     * @param endName the destination vertex name
     * @return the shortest path as a List<Vertex>
     */

    public List<Vertex> getShortestPath(String endName){
        if (!graphMap.containsKey(endName)) {
            throw new DijkstraGraphMapException("Graph doesn't contain end vertex : "+endName);
        }

        return graphMap.get(endName).getShortestPathTo();
    }

    // Implementation of dijkstra's algorithm using a binary heap.
    private void dijkstra(final NavigableSet<Vertex> que) {
        Vertex source, neighbour;
        while (!que.isEmpty()) {

            source = que.pollFirst(); // vertex with shortest distance (first iteration will return source)
            if (source.getTime() == Integer.MAX_VALUE) break; // ignore u (and any other remaining vertices) since they are unreachable

            //look at distances to each neighbour
            for (Map.Entry<Vertex, Integer> a : source.getNeighbours().entrySet()) {
                neighbour = a.getKey();

                final int alternateTime = source.getTime() + a.getValue();
                if (alternateTime < neighbour.getTime()) { // shorter path to neighbour found
                    que.remove(neighbour);
                    neighbour.setTime(alternateTime);
                    neighbour.setPrevVertext(source);
                    que.add(neighbour);
                }
            }
        }
    }
}
