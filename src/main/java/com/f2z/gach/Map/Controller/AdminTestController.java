package com.f2z.gach.Map.Controller;

import com.f2z.gach.Map.DTO.NavigationResponseDTO;
import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.Map.Entity.PlaceSource;
import com.f2z.gach.Map.Repository.MapLineRepository;
import com.f2z.gach.Map.Repository.MapNodeRepository;
import com.f2z.gach.Map.Repository.PlaceSourceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/map")
@SessionAttributes
@Transactional
public class AdminTestController {
    private final PlaceSourceRepository placeSourceRepository;
    private final MapLineRepository mapLineRepository;
    private final MapNodeRepository mapNodeRepository;
    private final String routeTypeShortest = "SHORTEST";
    private final String routeTypeOptimal = "OPTIMAL";

    @GetMapping("/test")
    public String testPage(Model model) {
        model.addAttribute("nodeDto", new NavigationResponseDTO.AdminMapNode());
        model.addAttribute("nodeList", new NavigationResponseDTO.AdminNodeList());
        return "test/test.html";
    }


    @GetMapping("/test")
    public String getTestRoute(@RequestParam Integer departures, @RequestParam Integer arrivals,
                               Model model){
        PlaceSource departuresPlace = placeSourceRepository.findByPlaceId(departures);
        departures = getNearestNodeId(departuresPlace.getPlaceLatitude(),
                departuresPlace.getPlaceLongitude(),
                departuresPlace.getPlaceAltitude());

        PlaceSource arrivalsPlace = placeSourceRepository.findByPlaceId(arrivals);
        arrivals = getNearestNodeId(arrivalsPlace.getPlaceLatitude(),
                arrivalsPlace.getPlaceLongitude(),
                arrivalsPlace.getPlaceAltitude());
        List<NavigationResponseDTO.NodeDTO> shortestRoute  = calculateRoute(routeTypeShortest, departures, arrivals);
        List<NavigationResponseDTO.NodeDTO> optimalRoute = calculateRoute(routeTypeOptimal, departures, arrivals);

        model.addAttribute("nodeDto", NavigationResponseDTO.toAdminMapNode(mapNodeRepository.findByNodeId(departures), mapNodeRepository.findByNodeId(arrivals)));
        model.addAttribute("nodeList", NavigationResponseDTO.toAdminNodeList(shortestRoute,optimalRoute));
        return "redirect:/test/test.html";
    }



    private Integer getNearestNodeId(Double placeLatitude, Double placeLongitude, Double placeAltitude) {
        int resultId = 0;
        final int R = 6371; // 지구의 반지름 (킬로미터)

        double minDistance= Double.MAX_VALUE;
        List<MapNode> nodeList = mapLineRepository.findAll().stream().map(MapLine::getNodeFirst).toList();
        for(MapNode node : nodeList){
            double c = getHaversine(placeLatitude, placeLongitude, node);
            double distance = R * c; // 거리 (킬로미터)

            if (minDistance > distance) {
                minDistance = distance;
                resultId = node.getNodeId();
            }
        }
        return resultId;
    }

    private static double getHaversine(Double placeLatitude, Double placeLongitude, MapNode node) {
        double latDistance = Math.toRadians(node.getNodeLatitude() - placeLatitude);
        double lonDistance = Math.toRadians(node.getNodeLongitude() - placeLongitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(placeLatitude)) * Math.cos(Math.toRadians(node.getNodeLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c;
    }

    private List<NavigationResponseDTO.NodeDTO> calculateRoute(String routeType, Integer departuresId, Integer arrivalsId) {
        List<NavigationResponseDTO.NodeDTO> nodeList = new ArrayList<>();
        Map<Integer, Double> distances = new HashMap<>();
        Map<Integer, Integer> previousNodes = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        distances.put(departuresId, 0.0);
        priorityQueue.offer(departuresId);

        while (!priorityQueue.isEmpty()) {
            int currentNodeId = priorityQueue.poll();
            if (currentNodeId == arrivalsId) break;
            if (visited.contains(currentNodeId)) continue;
            visited.add(currentNodeId);

            for (MapLine edge : mapLineRepository.findAllByNodeFirst_NodeId(currentNodeId)) {
                double weight = routeType.equals(routeTypeShortest) ? edge.getWeightShortest() : edge.getWeightOptimal() + 180;
                int neighborNodeId = edge.getNodeFirst().getNodeId() == currentNodeId ? edge.getNodeSecond().getNodeId() : edge.getNodeFirst().getNodeId();
                double distanceThroughCurrent = distances.getOrDefault(currentNodeId, Double.MAX_VALUE) + weight;

                if (distanceThroughCurrent < distances.getOrDefault(neighborNodeId, Double.MAX_VALUE)) {
                    distances.put(neighborNodeId, distanceThroughCurrent);
                    previousNodes.put(neighborNodeId, currentNodeId);
                    priorityQueue.offer(neighborNodeId);
                }
            }
        }

        // 경로 역추적
        int currentNodeId = arrivalsId;
        while (previousNodes.containsKey(currentNodeId)) {
            MapNode node = mapNodeRepository.findById(currentNodeId).orElseThrow(() -> new NoSuchElementException("Node not found"));
            nodeList.add(NavigationResponseDTO.NodeDTO.builder()
                    .nodeId(node.getNodeId())
                    .latitude(node.getNodeLatitude())
                    .longitude(node.getNodeLongitude())
                    .altitude(node.getNodeAltitude())
                    .build());
            currentNodeId = previousNodes.get(currentNodeId);
        }
        nodeList.add(NavigationResponseDTO.NodeDTO.builder()
                .nodeId(departuresId)
                .latitude(mapNodeRepository.findByNodeId(departuresId).getNodeLatitude())
                .longitude(mapNodeRepository.findByNodeId(departuresId).getNodeLongitude())
                .altitude(mapNodeRepository.findByNodeId(departuresId).getNodeAltitude())
                .build());
        Collections.reverse(nodeList);

        return nodeList;
    }
}
