package com.planday.deliveroo.service;

import android.support.v4.util.Pair;
import android.util.Log;

import com.planday.deliveroo.connector.GoogleServerConnector;
import com.planday.deliveroo.model.MapPlace;
import com.planday.deliveroo.model.response.direction.GeocodedWaypoints;
import com.planday.deliveroo.model.response.direction.Leg;
import com.planday.deliveroo.model.response.direction.Route;
import com.planday.deliveroo.model.response.distance.DestinationAddresses;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by longnguyen on 9:05 PM, 7/20/18.
 */
public class GoogleService {

    private GoogleServerConnector googleServerConnector;

    private int shortestDistance;
    private int longestDistance;
    private List<Integer>  shortestPath    = null;
    private List<MapPlace> reorderedPlaces = new ArrayList<>();
    private int[][] distanceMatrix;

    public GoogleService() {
        googleServerConnector = new GoogleServerConnector();
    }

    public Observable<Pair<List<String[]>, List<MapPlace>>> getDirections(final List<MapPlace> originalPlaces, String unit, String origins, String destinations,
                                                                          final String mode, final String apiKey) {
        return googleServerConnector.getDistanceMatrix(unit, origins, destinations, mode, apiKey)
                                    .map(new Func1<DestinationAddresses, List<Integer>>() {
                                        @Override
                                        public List<Integer> call(DestinationAddresses destinationAddresses) {
                                            distanceMatrix = generateDistanceMatrix(destinationAddresses);
                                            while (shortestPath == null) {
                                                calculateShortestPath(distanceMatrix);
                                            }
                                            return shortestPath;
                                        }
                                    })
                                    .flatMap(new Func1<List<Integer>, Observable<GeocodedWaypoints>>() {
                                        @Override
                                        public Observable<GeocodedWaypoints> call(List<Integer> shortestPath) {
                                            reorderedPlaces = new ArrayList<>();
                                            for (int i : shortestPath) {
                                                reorderedPlaces.add(originalPlaces.get(i));
                                            }
                                            String origin = reorderedPlaces.get(0).getLatitude() + "," + reorderedPlaces.get(0).getLongitude();

                                            StringBuilder wayPoints = new StringBuilder();
                                            for (MapPlace mapPlace : reorderedPlaces) {
                                                wayPoints.append(mapPlace.getLatitude()).append(",").append(mapPlace.getLongitude()).append("|");
                                            }
                                            return googleServerConnector.getDirection(origin, origin, wayPoints.toString(), mode, apiKey);
                                        }
                                    })
                                    .map(new Func1<GeocodedWaypoints, Pair<List<String[]>, List<MapPlace>>>() {
                                        @Override
                                        public Pair<List<String[]>, List<MapPlace>> call(GeocodedWaypoints geocodedWaypoints) {
                                            return Pair.create(getRoute(geocodedWaypoints), reorderedPlaces);
                                        }
                                    });
    }

    private int[][] generateDistanceMatrix(DestinationAddresses result) {
        int[][] matrix = new int[result.getRows().size()][result.getRows().size()];
        for (int i = 0; i < result.getRows().size(); i++) {
            for (int j = 0; j < result.getRows().get(i).getElements().size(); j++) {
                matrix[i][j] = result.getRows().get(i).getElements().get(j).getDistance().getValue();
            }
        }

        return matrix;
    }

    private void calculateShortestPath(int[][] matrix) {
        shortestDistance = Integer.MAX_VALUE;
        longestDistance = Integer.MIN_VALUE;

        int totalPlaces = matrix.length;

        ArrayList<Integer> places = new ArrayList<>();
        for (int i = 0; i < totalPlaces; i++) {
            places.add(i);
        }
        int startPlace = places.get(0);
        int currentDistance = 0;

        routeSearch(matrix, startPlace, currentDistance, places);
    }

    private void routeSearch(int[][] matrix, int startPlace, int currentDistance, ArrayList<Integer> places) {
        if (startPlace < places.size() - 1) {
            for (int i = startPlace; i < places.size(); i++) {
                int tempPlace = places.get(i);
                places.set(i, places.get(startPlace));
                places.set(startPlace, tempPlace);
                currentDistance = calculateDistance(places, matrix);
                routeSearch(matrix, startPlace + 1, currentDistance, places);
                tempPlace = places.get(i);
                places.set(i, places.get(startPlace));
                places.set(startPlace, tempPlace);
            }
        } else {
            if (shortestDistance > currentDistance) {
                shortestDistance = currentDistance;
                shortestPath = new ArrayList<>(places);
            }
            if (longestDistance < currentDistance) {
                longestDistance = currentDistance;
            }
        }
    }

    private static int calculateDistance(List<Integer> places, int[][] matrix) {
        int distance = 0;
        for (int i = 0; i < places.size() - 1; i++) {
            distance = distance + matrix[places.get(i)][places.get(i + 1)];
        }
        distance = distance + matrix[places.get(places.size() - 1)][places.get(0)];
        return distance;
    }

    private List<String[]> getRoute(GeocodedWaypoints data) {
        List<String[]> result = new ArrayList<>();
        String[] polylines;
        for (Route route : data.getRoutes()) {
            for (Leg leg : route.getLegs()) {
                polylines = new String[leg.getSteps().size()];
                for (int i = 0; i < leg.getSteps().size(); i++) {
                    polylines[i] = leg.getSteps().get(i).getPolyline().getPoints();
                }
                result.add(polylines);
            }
        }
        return result;
    }

    public int getShortestDistance() {
        return shortestDistance;
    }

    public int getLongestDistance() {
        return longestDistance;
    }

    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }
}
