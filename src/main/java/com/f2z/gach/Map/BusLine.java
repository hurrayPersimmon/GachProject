package com.f2z.gach.Map;
import com.f2z.gach.Map.Entity.MapNode;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class BusLine {
    private static final String busUpPath = "src/main/resources/static/busLine/BusLineUP.csv";
    private static final String busDownPath = "src/main/resources/static/busLine/BusLineDown.csv";

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Points {
        private MapNode departures;
        private MapNode arrivals;
        private Integer departuresIndex;
        private Integer arrivalsIndex;
        private String csvPath;
        private Integer totalTime;

        public static Points toPoints(MapNode departures, MapNode arrivals) {
            return Points.builder()
                    .departures(departures)
                    .arrivals(arrivals)
                    .build();
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Node{
        private Double latitude;
        private Double longitude;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ResultList{
        private List<Node> busLine;
        private Integer totalTime;
    }

    public static ResultList getBusLine(MapNode departures, MapNode arrivals) throws Exception {
        Points points = Points.toPoints(departures, arrivals);
        Points updatedPoints = getNearestNode(points, busDownPath, busUpPath);
        if(updatedPoints == null){
            return null;
        }
        return getNodeList(updatedPoints);
    }

    private static Points getNearestNode(Points points, String busDownPath, String busUpPath) throws Exception {
        points.setDeparturesIndex(compareDistance(points.getDepartures().getNodeLongitude(), points.getDepartures().getNodeLatitude(), busUpPath));
        points.setArrivalsIndex(compareDistance(points.getArrivals().getNodeLongitude(), points.getArrivals().getNodeLatitude(), busUpPath));
        points.setCsvPath(busUpPath);

        if(points.getDeparturesIndex() >= points.getArrivalsIndex()) {
            points.setDeparturesIndex(compareDistance(points.getDepartures().getNodeLongitude(), points.getDepartures().getNodeLatitude(), busDownPath));
            points.setArrivalsIndex(compareDistance(points.getArrivals().getNodeLongitude(), points.getArrivals().getNodeLatitude(), busDownPath));
            points.setCsvPath(busDownPath);

            if(points.getDeparturesIndex() >= points.getArrivalsIndex()) return null;
        }

        return points;
    }

    private static Integer compareDistance(Double longitude, Double latitude, String csvPath) throws IOException {
        Reader reader = new FileReader(csvPath);
        CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);
        double minDistance = Double.MAX_VALUE;
        int index = 0;
        int resultIndex = 0;
        String result = "";

        for (CSVRecord record : csvParser) {
            if (record.get("Line").equals("STOP")) {
                double distance = getDistance(longitude, latitude, Double.parseDouble(record.get("X")), Double.parseDouble(record.get("Y")));
                if (distance < minDistance) {
                    minDistance = distance;
                    resultIndex = index;
                    result = record.toString();
                }
            }
            index++;
        }
        csvParser.close();
        reader.close();
        return resultIndex;
    }

    private static ResultList getNodeList(Points points) throws IOException {
        Reader reader = new FileReader(points.getCsvPath());
        CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);
        List<Node> busLine = new ArrayList<>();
        int index = 0;
        int TotalTime = 0;
        for (CSVRecord record : csvParser){
            if(index >= points.getDeparturesIndex() && index <= points.getArrivalsIndex()){
                Node node = Node.builder()
                        .latitude(Double.parseDouble(record.get("Y")))
                        .longitude(Double.parseDouble(record.get("X")))
                        .build();
                busLine.add(node);
                if(record.get("Line").equals("STOP")){
                    TotalTime += Integer.parseInt(record.get("Time"));
                }
            }
            index++;
        }
        log.info("BusTime : " + TotalTime);;
        return ResultList.builder().busLine(busLine).totalTime(TotalTime).build();
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



}
