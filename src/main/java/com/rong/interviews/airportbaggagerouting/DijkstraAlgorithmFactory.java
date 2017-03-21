package com.rong.interviews.airportbaggagerouting;

/**
 * Created by rongyj on 3/21/17.
 */
public class DijkstraAlgorithmFactory {

    public static DijkstraAlgorithm createDijkstraAlgorithm(){
        return new DijkstraAlgorithmImpl();
    }
}
