package com.f2z.gach.Map;
import com.f2z.gach.Map.Entity.MapNode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class BusLine {
    private static final String busUpPath = "src/main/resources/busLineUp.csv";
    private static final String busDownPath = "src/main/resources/busLineDown.csv";

//    List
//
//    public static Map<String, Double> getBusLine(MapNode departures, MapNode arrivals){
//
//        return busLine;
//    }

    public void getNearestNode(MapNode departures, MapNode arrivals, String csvFilePath) throws Exception {
        Reader reader = new FileReader(csvFilePath);
        CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

        // 상행선에서 가장 가까운 출발지와 도착지 정류장(STOP)을 저장할 맵
        Map<String, Double> nearestDepartureNode = null;
        Map<String, Double> nearestArrivalNode = null;
        double minDepartureDistance = Double.MAX_VALUE;
        double minArrivalDistance = Double.MAX_VALUE;
        int departureIndex = 0;
        int arrivalIndex = 0;

        // 상행선에서 출발지와 도착지 근처의 정류장(STOP)을 찾음
        for (CSVRecord record : csvParser) {
            double longitude = Double.parseDouble(record.get("Y"));
            double latitude = Double.parseDouble(record.get("X"));
            String line = record.get("Line");

            // 출발지 근처의 정류장(STOP) 찾기
            if (line.equals("STOP")) {
                double distance = getDistance(departures.getNodeLongitude(), departures.getNodeAltitude(), longitude, latitude);
                if (distance < minDepartureDistance) {
                    minDepartureDistance = distance;
                    nearestDepartureNode = new HashMap<>();
                    nearestDepartureNode.put("latitude", latitude);
                    nearestDepartureNode.put("longitude", longitude);
                    departureIndex++;
                }
            }

            if (line.equals("STOP")) {
                double distance = getDistance(arrivals.getNodeLongitude(), arrivals.getNodeLatitude(), longitude, latitude);
                if (distance < minArrivalDistance) {
                    minArrivalDistance = distance;
                    nearestArrivalNode = new HashMap<>();
                    nearestArrivalNode.put("latitude", latitude);
                    nearestArrivalNode.put("longitude", longitude);
                    arrivalIndex++;
                }
            }
        }

        // 상행선 CSV 파일을 닫음
        csvParser.close();
        reader.close();


        // 출발지와 도착지가 같은 경우 null 반환
        if (nearestDepartureNode.equals(nearestArrivalNode) ||
                nearestDepartureNode == null ||
                nearestArrivalNode == null ||
                departureIndex > arrivalIndex) {
            return;
        }

        // 경로 탐색
        List<Map<String, Double>> path = findPath(nearestDepartureNode, nearestArrivalNode, csvFilePath);
//        return path;
    }

    private static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        final double R = 6371; // 지구의 반지름 (킬로미터)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 거리 (킬로미터)
    }

    private static List<Map<String, Double>> findPath(Map<String, Double> departure, Map<String, Double> arrival, String csvFilePath) throws Exception {
        // 경로를 저장할 리스트
        List<Map<String, Double>> path = new ArrayList<>();

        // 상행선 CSV 파일을 Reader로 열기
        Reader reader = new FileReader(csvFilePath);
        CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

        // 출발지와 도착지 사이의 정류장(STOP)을 찾아 경로에 추가
        boolean isDepartureFound = false;
        boolean isArrivalFound = false;
        for (CSVRecord record : csvParser) {
            double longitude = Double.parseDouble(record.get("Y"));
            double latitude = Double.parseDouble(record.get("X"));
            String line = record.get("Line");

            // 출발지 정류장(STOP)을 찾으면 출발지로 설정
            if (!isDepartureFound && departure.get("latitude").equals(latitude) && departure.get("longitude").equals(longitude)) {
                isDepartureFound = true;
                path.add(departure);
            }

            // 출발지 이후 도착지까지의 정류장(STOP)을 경로에 추가
            if (isDepartureFound && !isArrivalFound && line.equals("STOP")) {
                path.add(Map.of("latitude", latitude, "longitude", longitude));
            }

            // 도착지 정류장(STOP)을 찾으면 도착지로 설정하고 경로 탐색 종료
            if (!isArrivalFound && arrival.get("latitude").equals(latitude) && arrival.get("longitude").equals(longitude)) {
                isArrivalFound = true;
                path.add(arrival);
                break;
            }
        }

        // 상행선 CSV 파일을 닫음
        csvParser.close();
        reader.close();

        return path;
    }

}
