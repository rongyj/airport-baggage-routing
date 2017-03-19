package com.rong.interviews.airportbaggagerouting;

import com.rong.interviews.airportbaggagerouting.model.Bag;
import com.rong.interviews.airportbaggagerouting.model.DijkstraGraphMap;
import com.rong.interviews.airportbaggagerouting.model.DirectedEdge;
import com.rong.interviews.airportbaggagerouting.model.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rongyj on 3/17/17.
 */
public class Main {
    private final static String INPUT_DATA_SECTION_HEAD="# Section:";
    private final static String FLIGHT_ARRIVAL ="ARRIVAL";
    private final static String DEST_BAGGAGE_CLAIM ="BaggageClaim";
    private final static String SINGLE_WHITE_SPACE=" ";
    public static void main(String [] args){
        Scanner scan=null;
        if(args.length >0) {
            File inputDataFile=new File(args[0].trim());
            if(inputDataFile.exists()){
                try {
                    scan = new Scanner(inputDataFile);
                }catch (FileNotFoundException fnfex){
                    scan=promptAndParse();
                }
            }else{
               scan=promptAndParse();
            }
        }else
            scan=promptAndParse();
        if(scan != null){
            List<DirectedEdge> edges= parseInputGraph(scan);
            Map<String,String> departuresMap=parseInputDepartures(scan); //Map with the flight as the key and the destination gate as the value
            List<Bag> bagList = parseInputBags(scan);
            scan.close();
            Map<String, DijkstraGraphMap> visitedMap=new ConcurrentHashMap<>(); // Visited map with the sourceName as the key
            for(Bag bag:bagList){
                String bagId=bag.getId();
                String entryGate=bag.getEntryGate();
                String flight = bag.getFlight();
                DijkstraGraphMap dijkstraGraphMap;
                if(visitedMap.containsKey(entryGate)){
                    dijkstraGraphMap = visitedMap.get(entryGate);
                }else {
                    dijkstraGraphMap = new DijkstraGraphMap(edges);
                    dijkstraGraphMap.dijkstra(entryGate);
                    visitedMap.put(entryGate,dijkstraGraphMap);
                }
                String destGate;
                if(flight.equals(FLIGHT_ARRIVAL)){
                    destGate=DEST_BAGGAGE_CLAIM;
                }else{
                    destGate=departuresMap.get(flight);
                }
                List<Vertex> shortedPath= dijkstraGraphMap.getShortestPath(destGate);
                outputPathLine(bagId,shortedPath);
            }
        }

    }

    private static void outputPathLine(String bagId, List<Vertex> path){
        StringBuffer line = new StringBuffer(bagId).append(SINGLE_WHITE_SPACE);

        for(Vertex vertex:path){
            line.append(vertex.getName()).append(SINGLE_WHITE_SPACE);
        }
        line.append(": ").append(path.get(path.size()-1).getTime());
        System.out.println(line.toString());
    }

    private static Scanner promptAndParse(){
        System.out.println("Please input the data here:");
        Scanner scan = new Scanner(System.in);
        return scan;
    }

    private static List<DirectedEdge> parseInputGraph(Scanner scanner){
        String graphSection=scanner.nextLine();
        if(!graphSection.startsWith(INPUT_DATA_SECTION_HEAD)){
            throw new IllegalArgumentException("Illegal arguments or inputs. Please refer to readme for the input data format.");
        }
        String next=scanner.nextLine();
        List<DirectedEdge> edges=new ArrayList<>();
        while (!next.startsWith(INPUT_DATA_SECTION_HEAD)){
            String[] parts=next.trim().split("\\s+");
            if(parts.length>=3) {
                DirectedEdge directedEdge = new DirectedEdge(parts[0],parts[1],Integer.valueOf(parts[2]));
                edges.add(directedEdge);
                //Since it is bi-direction edge, will add another direction edge too.
                DirectedEdge rDirectedEdge = new DirectedEdge(parts[1],parts[0],Integer.valueOf(parts[2]));
                edges.add(rDirectedEdge);
            }else{
                throw new IllegalArgumentException("Illegal arguments or inputs. Please refer to readme for the input data format.");
            }
            next=scanner.nextLine();
        }
        return edges;
    }

    private static Map<String,String> parseInputDepartures(Scanner scanner){
        String next=scanner.nextLine();
        Map<String,String> departuresMap=new HashMap<>();
        while(!next.startsWith(INPUT_DATA_SECTION_HEAD)){
            String [] parts=next.trim().split("\\s+");
            if(parts.length >= 2){
                departuresMap.put(parts[0],parts[1]);
            }else{
                throw new IllegalArgumentException("Illegal arguments or inputs. Please refer to readme for the input data format.");
            }
            next=scanner.nextLine();
        }
        return departuresMap;
    }

    private static List<Bag> parseInputBags(Scanner scanner){
        String next ;
        List<Bag> bagList= new ArrayList<>();
        do{
            next = scanner.nextLine();
            String [] parts = next.trim().split("\\s+");
            if(parts.length >=3){
                Bag bag= new Bag(parts[0],parts[1],parts[2]);
                bagList.add(bag);
            }else{
                scanner.close();
                break;
            }
        }while(scanner.hasNextLine());
        return bagList;
    }
}
