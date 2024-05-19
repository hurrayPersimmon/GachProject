package com.f2z.gach.Map.Controller;

import com.f2z.gach.AI.AIService;
import com.f2z.gach.Admin.Repository.AdminRepository;
import com.f2z.gach.DataGetter.dataEntity;
import com.f2z.gach.EnumType.Authorization;
import com.f2z.gach.Inquiry.Repository.InquiryRepository;
import com.f2z.gach.Map.DTO.NavigationResponseDTO;
import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.Map.Entity.PlaceSource;
import com.f2z.gach.Map.Repository.MapLineRepository;
import com.f2z.gach.Map.Repository.MapNodeRepository;
import com.f2z.gach.Map.Repository.PlaceSourceRepository;
import com.f2z.gach.Map.Service.MapServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;
    private final String routeTypeShortest = "SHORTEST";
    private final String routeTypeOptimal = "OPTIMAL";
    private final AIService aiService;

    @ModelAttribute
    public void setAttributes(Model model){
        model.addAttribute("waiterListSize", adminRepository.findByAdminAuthorization(Authorization.WAITER).size());
        model.addAttribute("inquiryWaitSize", inquiryRepository.countByInquiryProgressIsFalse());
        model.addAttribute("lineSize", mapLineRepository.count());
    }

    @GetMapping("/test")
    public String testPage(Model model) {
        model.addAttribute("MapNodeList", mapNodeRepository.findAll());
        model.addAttribute("nodeDto", new NavigationResponseDTO.AdminMapNode());
        model.addAttribute("nodeList", new NavigationResponseDTO.AdminNodeList());
        return "test/pathTest";
    }


    @GetMapping("/test/result")
    public String getTestRoute(@RequestParam Integer arrivals, @RequestParam Integer departures,
                               Model model) throws Exception {
        log.info("getTestRoute");
        MapNode departuresPlace = mapNodeRepository.findByNodeId(departures);
        departures = getNearestNodeId(departuresPlace.getNodeLatitude(),
                departuresPlace.getNodeLongitude(),
                departuresPlace.getNodeAltitude());

        MapNode arrivalsPlace = mapNodeRepository.findByNodeId(arrivals);
        arrivals = getNearestNodeId(arrivalsPlace.getNodeLatitude(),
                arrivalsPlace.getNodeLongitude(),
                arrivalsPlace.getNodeAltitude());
        List<NavigationResponseDTO.NodeDTO> shortestRoute  = calculateRoute(routeTypeShortest, departures, arrivals);
        List<NavigationResponseDTO.NodeDTO> optimalRoute = calculateRoute(routeTypeOptimal, departures, arrivals);

        model.addAttribute("arrivals", mapNodeRepository.findByNodeId(arrivals));
        model.addAttribute("departures", mapNodeRepository.findByNodeId(departures));
        model.addAttribute("nodeDto", NavigationResponseDTO.toAdminMapNode(mapNodeRepository.findByNodeId(departures), mapNodeRepository.findByNodeId(arrivals)));
        model.addAttribute("nodeList", NavigationResponseDTO.toAdminNodeList(shortestRoute,optimalRoute));

        dataEntity data = new dataEntity();
        data.setGender(0);
        data.setTemperature(19.4);
        data.setPrecipitationProbability(10);
        data.setPrecipitation(10.0);
        data.setBirthYear(2000);
        data.setWeight(52.1);
        data.setHeight(165.2);
        data.setWalkSpeed(1);

        var ref = new Object() {
            double optimalTakeTime;
            double shortestTakeTime;
        };
        log.info(String.valueOf(shortestRoute.size()));
        ExecutorService shortExecutor = Executors.newFixedThreadPool(shortestRoute.size());
        ExecutorService optimalExecutor = Executors.newFixedThreadPool(optimalRoute.size());

        List<Future<Double>> shortFutures = new ArrayList<>();
        List<Future<Double>> optimalFutures = new ArrayList<>();

        for(int i = 0; i < shortestRoute.size()-1; i++) {
            MapLine line = mapLineRepository.findLineIdByNodeFirst_NodeIdAndNodeSecond_NodeId(shortestRoute.get(i).getNodeId(), shortestRoute.get(i+1).getNodeId());
            shortFutures.add(shortExecutor.submit( () -> aiService.modelOutput(line, data)));
        }

        for (Future<Double> future : shortFutures) {
            try {
                ref.shortestTakeTime += future.get();
            } catch (InterruptedException | ExecutionException e) {
            }
        }

        shortExecutor.shutdown();

        for(int i = 0; i < optimalRoute.size()-1; i++) {
            MapLine line = mapLineRepository.findLineIdByNodeFirst_NodeIdAndNodeSecond_NodeId(optimalRoute.get(i).getNodeId(), optimalRoute.get(i+1).getNodeId());
            optimalFutures.add(optimalExecutor.submit( () -> aiService.modelOutput(line, data)));
        }

        for (Future<Double> future : optimalFutures) {
            try {
                ref.optimalTakeTime += future.get();
            } catch (InterruptedException | ExecutionException e) {
                // 예외 처리
            }
        }

        model.addAttribute("shortTakeTime", ref.shortestTakeTime);
        model.addAttribute("optimalTakeTime", ref.optimalTakeTime);
        return "test/pathTestResult";
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
        MapServiceImpl.routeBackTracking(departuresId, arrivalsId, nodeList, previousNodes, mapNodeRepository);

        return nodeList;
    }

}
