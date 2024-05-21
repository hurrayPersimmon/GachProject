package com.f2z.gach.Map.Service;

import com.f2z.gach.AI.AIService;
import com.f2z.gach.EnumType.*;
import com.f2z.gach.Map.BusLine;
import com.f2z.gach.Map.DTO.NavigationResponseDTO;
import com.f2z.gach.Map.DTO.PlaceRequestDTO;
import com.f2z.gach.Map.DTO.PlaceResponseDTO;
import com.f2z.gach.Map.Entity.*;
import com.f2z.gach.Map.Repository.*;
import com.f2z.gach.Response.ResponseEntity;
import com.f2z.gach.Response.ResponseListEntity;
import com.f2z.gach.User.Entity.User;
import com.f2z.gach.User.Entity.UserGuest;
import com.f2z.gach.User.Repository.UserGuestRepository;
import com.f2z.gach.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Integer.parseInt;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class MapServiceImpl implements MapService{
    private final PlaceSourceRepository placeSourceRepository;
    private final BuildingFloorRepository buildingFloorRepository;
    private final BuildingKeywordRepository buildingKeywordRepository;
    private final MapLineRepository mapLineRepository;
    private final MapNodeRepository mapNodeRepository;
    private final UserRepository userRepository;
    private final UserGuestRepository userGuestRepository;
    private final AIService aiService;

    private final String routeTypeShortest = "SHORTEST";
    private final String routeTypeOptimal = "OPTIMAL";
    private final String routeBus = "busRoute";



    @Override
    public ResponseEntity<PlaceResponseDTO.respondPlaceList> getBuildingInfoList() {
        List<PlaceSource> buildingInfoList = placeSourceRepository.findAllByPlaceCategory(PlaceCategory.BUILDING);
        if(buildingInfoList.isEmpty()){
            return ResponseEntity.notFound(null);
        }else {
            return ResponseEntity.requestSuccess(PlaceResponseDTO
                    .toRespondPlaceList(buildingInfoList));
        }
    }


    @Override
    public ResponseEntity<PlaceResponseDTO.toRespondBuildingInfo> getBuildingInfo(Integer placeId) {
        PlaceSource buildingInfo = placeSourceRepository.findByPlaceId(placeId);
        if(buildingInfo != null){
            List<BuildingFloor> buildingFloors= buildingFloorRepository.findAllByPlaceSource_placeId(placeId);
            return ResponseEntity.requestSuccess(PlaceResponseDTO.toRespondBuildingInfo(
                    buildingInfo,PlaceResponseDTO.toBuildingInfoStructureList(buildingFloors)));
        }else {
            return ResponseEntity.notFound(null);
        }
    }


    @Override
    public ResponseEntity<List<PlaceResponseDTO.respondKeywordList>> getKeywordResult(String target) {
        // 1. 장소 이름 자체를 검색하는 경우
        // 1-2. 카테고리로 검색
        try {placeSourceRepository.findPlaceSourcesByPlaceCategoryContaining(PlaceCategory.valueOf(target));
            return ResponseEntity.requestSuccess(PlaceResponseDTO.toKeywordList(placeSourceRepository.findPlaceSourcesByPlaceCategoryContaining(PlaceCategory.valueOf(target))));
        } catch (IllegalArgumentException e) {
            log.info("Not a category");
        }
        // 1-1. 건물 이름으로 검색
        if(!placeSourceRepository.findPlaceSourcesByPlaceNameContaining(target).isEmpty()){
            List<PlaceSource> targetList = placeSourceRepository.findPlaceSourcesByPlaceNameContaining(target);
            return ResponseEntity.requestSuccess(PlaceResponseDTO.toKeywordList(targetList));
        }

        // 2. 키워드로 검색하는 경우
        // 2-1. 학과로 검색
        try {
            BuildingKeyword keyword = buildingKeywordRepository.findByDepartmentContaining(Departments.valueOf(target));
            PlaceSource targetPlace = placeSourceRepository.findByPlaceId(keyword.getPlaceSource().getPlaceId());
            return ResponseEntity.requestSuccess(Collections.singletonList(PlaceResponseDTO.toKeywordList(targetPlace)));
        } catch (IllegalArgumentException e){
            log.info("Not a department");
        }
        // 2-2. 단과대학으로 검색
        try{
            BuildingKeyword keyword = buildingKeywordRepository.findByCollegeContaining(College.valueOf(target));
            PlaceSource targetPlace = placeSourceRepository.findByPlaceId(keyword.getPlaceSource().getPlaceId());
            return ResponseEntity.requestSuccess(Collections.singletonList(PlaceResponseDTO.toKeywordList(targetPlace)));
        } catch (IllegalArgumentException e){
            log.info("Not a college");
        }

        // 2-3. 교수님 성함으로 검색
        if (buildingKeywordRepository.findByProfessorNameContaining(target) != null) {
            BuildingKeyword keyword = buildingKeywordRepository.findByProfessorNameContaining(target);
            PlaceSource targetPlace = placeSourceRepository.findByPlaceId(keyword.getPlaceSource().getPlaceId());
            targetPlace.setPlaceSummary(keyword.getProfessorClass());
            return ResponseEntity.requestSuccess(Collections.singletonList(PlaceResponseDTO.toKeywordList(targetPlace)));
        }
        else {
            return ResponseEntity.saveButNoContent(null);
        }
    }

    @Override
    public ResponseEntity<PlaceResponseDTO.placeLocationDTO> getKeywordDetailResult(Integer placeId) {
        PlaceSource place = placeSourceRepository.findByPlaceId(placeId);
        if(place == null){
            return ResponseEntity.notFound(null);
        }else{
            return ResponseEntity.requestSuccess(PlaceResponseDTO.toPlaceLocationDTO(place));
        }
    }

    @Override
    public ResponseEntity<List<PlaceResponseDTO.placeLocationDTO>> getPlaceListByCategory(String placeCategory) {
        List<PlaceSource> placeList = placeSourceRepository.findAllByPlaceCategory(PlaceCategory.valueOf(placeCategory));
        if(placeList.isEmpty()) {
            return ResponseEntity.notFound(null);
        }else{
            return ResponseEntity.requestSuccess(PlaceResponseDTO.toPlaceLocationDTOList(placeList));
        }
    }

    @Setter
    class pointIds{
        Integer departuresNodeId;
        Integer arrivalsNodeId;
    }

    @Builder
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class AIData{
        Integer birthYear;
        Double height;
        Double weight;
        Speed walkSpeed;
        Gender gender;
        Double temperature;
        Double precipitation;
        Integer precipitationProbability;


    }

    @Override
    public ResponseListEntity<NavigationResponseDTO> getNowRoute(PlaceRequestDTO.requestLocation placeRequestDTO,
                                                                 Double latitude,
                                                                 Double longitude,
                                                                 Double altitude) throws Exception {
        pointIds pointIds = new pointIds();

        if(placeRequestDTO.isDepartures()) {
            pointIds.setArrivalsNodeId(getNearestNodeId(latitude, longitude, altitude));
            if(placeRequestDTO.isPlace()) {
                PlaceSource placeSource = placeSourceRepository.findByPlaceId(placeRequestDTO.getPlaceId());
                pointIds.setDeparturesNodeId(getNearestNodeId(placeSource.getPlaceLatitude(), placeSource.getPlaceLongitude(), placeSource.getPlaceAltitude()));
            }else{
                pointIds.setArrivalsNodeId(getNearestNodeId(placeRequestDTO.getLatitude(), placeRequestDTO.getLongitude(), placeRequestDTO.getAltitude()));
            }
        }else {
            pointIds.setDeparturesNodeId(getNearestNodeId(latitude, longitude, altitude));
            if(placeRequestDTO.isPlace()) {
                PlaceSource placeSource = placeSourceRepository.findByPlaceId(placeRequestDTO.getPlaceId());
                pointIds.setArrivalsNodeId(getNearestNodeId(placeSource.getPlaceLatitude(), placeSource.getPlaceLongitude(), placeSource.getPlaceAltitude()));
            }else{
                pointIds.setArrivalsNodeId(getNearestNodeId(placeRequestDTO.getLatitude(), placeRequestDTO.getLongitude(), placeRequestDTO.getAltitude()));
            }
        }

        NavigationResponseDTO shortestRoute = calculateRoute(routeTypeShortest, pointIds.departuresNodeId, pointIds.arrivalsNodeId, setAIData(placeRequestDTO));
        NavigationResponseDTO optimalRoute = calculateRoute(routeTypeOptimal, pointIds.departuresNodeId, pointIds.arrivalsNodeId, setAIData(placeRequestDTO));

        Integer shortestDepartureNodeId = shortestRoute.getNodeList().get(0).getNodeId();
        Integer shortestArrivalsNodeId = shortestRoute.getNodeList().get(shortestRoute.getNodeList().size()-1).getNodeId();
        if(shortestDepartureNodeId == 0 || shortestArrivalsNodeId == 0) return ResponseListEntity.notFound(null);
        if(Objects.equals(shortestDepartureNodeId, shortestArrivalsNodeId)) return ResponseListEntity.sameNode(null);
        NavigationResponseDTO busRoute = getBusRoute(shortestDepartureNodeId,shortestArrivalsNodeId, setAIData(placeRequestDTO));

        List<NavigationResponseDTO> routes;
        if(busRoute == null) routes = Arrays.asList(shortestRoute, optimalRoute);
        else routes = Arrays.asList(shortestRoute, optimalRoute, busRoute);

        return ResponseListEntity.requestListSuccess(routes.toArray(new NavigationResponseDTO[0]));
    }

    @Override
    public ResponseListEntity<NavigationResponseDTO> getRoute(PlaceRequestDTO.requestLocation placeRequestDTO,
                                                              Integer departures, Integer arrivals) throws Exception {
        PlaceSource departuresPlace = placeSourceRepository.findByPlaceId(departures);
        Integer departuresNodeId = getNearestNodeId(departuresPlace.getPlaceLatitude(),
                departuresPlace.getPlaceLongitude(),
                departuresPlace.getPlaceAltitude());

        PlaceSource arrivalsPlace = placeSourceRepository.findByPlaceId(arrivals);
        Integer arrivalsNodeId = getNearestNodeId(arrivalsPlace.getPlaceLatitude(),
                arrivalsPlace.getPlaceLongitude(),
                arrivalsPlace.getPlaceAltitude());

        NavigationResponseDTO shortestRoute = calculateRoute(routeTypeShortest, departuresNodeId, arrivalsNodeId,setAIData(placeRequestDTO));
        NavigationResponseDTO optimalRoute = calculateRoute(routeTypeOptimal, departuresNodeId, arrivalsNodeId,setAIData(placeRequestDTO));

        Integer shortestDepartureNodeId = shortestRoute.getNodeList().get(0).getNodeId();
        Integer shortestArrivalsNodeId = shortestRoute.getNodeList().get(shortestRoute.getNodeList().size()-1).getNodeId();
        if(shortestDepartureNodeId == 0 || shortestArrivalsNodeId == 0) return ResponseListEntity.notFound(null);
        if(Objects.equals(shortestDepartureNodeId, shortestArrivalsNodeId)) return ResponseListEntity.sameNode(null);
        NavigationResponseDTO busRoute = getBusRoute(shortestDepartureNodeId,shortestArrivalsNodeId,setAIData(placeRequestDTO));

        List<NavigationResponseDTO> routes;
        if(busRoute == null) routes = Arrays.asList(shortestRoute, optimalRoute);
        else routes = Arrays.asList(shortestRoute, optimalRoute, busRoute);

        return ResponseListEntity.requestListSuccess(routes.toArray(new NavigationResponseDTO[0]));
    }

    private AIData setAIData(PlaceRequestDTO.requestLocation placeRequestDTO) {
        if(placeRequestDTO.isUser()){
            return AIData.builder()
                    .birthYear(userRepository.findByUserId(placeRequestDTO.getUserId()).getUserBirth())
                    .height(userRepository.findByUserId(placeRequestDTO.getUserId()).getUserHeight())
                    .weight(userRepository.findByUserId(placeRequestDTO.getUserId()).getUserWeight())
                    .walkSpeed(userRepository.findByUserId(placeRequestDTO.getUserId()).getUserSpeed())
                    .temperature(placeRequestDTO.getTemperature())
                    .precipitation(placeRequestDTO.getPrecipitation())
                    .precipitationProbability(placeRequestDTO.getPrecipitationProbability())
                    .build();
        }else {
            return AIData.builder()
                    .birthYear(userGuestRepository.findByGuestId(placeRequestDTO.getGuestId()).getGuestBirth())
                    .height(userGuestRepository.findByGuestId(placeRequestDTO.getGuestId()).getGuestHeight())
                    .weight(userGuestRepository.findByGuestId(placeRequestDTO.getGuestId()).getGuestWeight())
                    .walkSpeed(userGuestRepository.findByGuestId(placeRequestDTO.getGuestId()).getGuestSpeed())
                    .temperature(placeRequestDTO.getTemperature())
                    .precipitation(placeRequestDTO.getPrecipitation())
                    .precipitationProbability(placeRequestDTO.getPrecipitationProbability())
                    .build();
        }
    }

    @Override
    public ResponseEntity<List<PlaceResponseDTO.placeARImageDTO>> getARImageList(Double latitude, Double longitude, Double altitude) {
        List<PlaceSource> placeList = placeSourceRepository.findAllByPlaceCategory(PlaceCategory.BUILDING);
        List<PlaceResponseDTO.placeARImageDTO> arImageList = new ArrayList<>();
        int nearestNodeId = getNearestNodeId(latitude, longitude, altitude);
        MapNode nearestNode = mapNodeRepository.findByNodeId(nearestNodeId);
        arImageList.add(PlaceResponseDTO.placeARImageDTO.builder()
                .placeId(0)
                .placeLatitude(nearestNode.getNodeLatitude())
                .placeLongitude(nearestNode.getNodeLongitude())
                .placeAltitude(nearestNode.getNodeAltitude())
                .buildingHeight(0.0)
                .arImagePath("")
                .build());

        for(PlaceSource place : placeList) arImageList.add(PlaceResponseDTO.placeARImageDTO.toPlaceARImageDTO(place));
        return ResponseEntity.requestSuccess(arImageList);
    }

    private NavigationResponseDTO getBusRoute(Integer shortestDepartureNodeId, Integer shortestArrivalsNodeId, AIData aiData) throws Exception {
        List<BusLine.Node> busLine;
        busLine = BusLine.getBusLine(mapNodeRepository.findByNodeId(shortestDepartureNodeId), mapNodeRepository.findByNodeId(shortestArrivalsNodeId));
        if(busLine == null|| busLine.isEmpty()) return NavigationResponseDTO.toNavigationResponseDTO(routeBus, null, new ArrayList<>());

        else{
            List<NavigationResponseDTO.NodeDTO> nodeList = new ArrayList<>();
            for(BusLine.Node node : busLine){
                nodeList.add(NavigationResponseDTO.NodeDTO.builder()
                        .nodeId(0)
                        .latitude(node.getLatitude())
                        .longitude(node.getLongitude())
                        .altitude(0.0)
                        .build());
            }
            int tailIndex = nodeList.size() -1;

            NavigationResponseDTO gettingOnRoute = calculateRoute(routeBus, shortestDepartureNodeId, getNearestNodeId(nodeList.get(0).getLatitude(), nodeList.get(0).getLongitude(), null), aiData);
            NavigationResponseDTO gettingOffRoute = calculateRoute(routeBus, getNearestNodeId(nodeList.get(tailIndex).getLatitude(), nodeList.get(tailIndex).getLongitude(), null),shortestArrivalsNodeId, aiData);
            List<NavigationResponseDTO.NodeDTO> busRouteMergedlist = new ArrayList<>();
            Integer totalTime = 0;
            totalTime += aiService.calculateTime(gettingOnRoute.getNodeList(), aiData);
            totalTime += aiService.calculateTime(gettingOffRoute.getNodeList(), aiData);

            if(!gettingOnRoute.getNodeList().isEmpty()) busRouteMergedlist.addAll(gettingOnRoute.getNodeList());
            if(!nodeList.isEmpty()) busRouteMergedlist.addAll(nodeList);
            if(!gettingOffRoute.getNodeList().isEmpty()) busRouteMergedlist.addAll(gettingOffRoute.getNodeList());
            NavigationResponseDTO busMergedRoute = NavigationResponseDTO.toNavigationResponseDTO(routeBus, totalTime, busRouteMergedlist);

            return busMergedRoute;
        }
    }


    private Integer getNearestNodeId(Double placeLatitude, Double placeLongitude, Double placeAltitude) {
        int resultId = 0;
        final int R = 6371; // 지구의 반지름 (킬로미터)

        double minDistance= Double.MAX_VALUE;
        List<MapNode> nodeList = mapLineRepository.findAll().stream().map(MapLine::getNodeFirst).toList();
        for(MapNode node : nodeList){
            double distance = getHaversine(placeLatitude, placeLongitude, node);
//            double distance;
//            if(placeAltitude == null) distance = getHaversine(placeLatitude, placeLongitude, node);
//            else distance = getVincenty(placeLatitude, placeLongitude, placeAltitude, node);

            if (minDistance > distance) {
                minDistance = distance;
                resultId = node.getNodeId();
            }
        }
        return resultId;
    }

    private static double getVincenty(Double placeLatitude, Double placeLongitude, Double placeAltitude, MapNode node) {
        final int R = 6371; // 지구의 반지름 (킬로미터)
        double lat1 = Math.toRadians(placeLatitude);
        double lon1 = Math.toRadians(placeLongitude);
        double lat2 = Math.toRadians(node.getNodeLatitude());
        double lon2 = Math.toRadians(node.getNodeLongitude());
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Math.sqrt(Math.pow(c * R,2) + Math.pow(Math.abs(placeAltitude - node.getNodeAltitude()),2));
    }

    private static double getHaversine(Double placeLatitude, Double placeLongitude, MapNode node) {
        final int R = 6371; // 지구의 반지름 (킬로미터)
        double latDistance = Math.toRadians(node.getNodeLatitude() - placeLatitude);
        double lonDistance = Math.toRadians(node.getNodeLongitude() - placeLongitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(placeLatitude)) * Math.cos(Math.toRadians(node.getNodeLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return c * R;
    }

    private NavigationResponseDTO calculateRoute(String routeType, Integer departuresNodeId, Integer arrivalsNodeId, AIData aIData) {
        List<NavigationResponseDTO.NodeDTO> nodeList = new ArrayList<>();
        Map<Integer, Double> distances = new HashMap<>();
        Map<Integer, Integer> previousNodes = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        distances.put(departuresNodeId, 0.0);
        priorityQueue.offer(departuresNodeId);

        while (!priorityQueue.isEmpty()) {
            int currentNodeId = priorityQueue.poll();
            if (currentNodeId == arrivalsNodeId) break;
            if (visited.contains(currentNodeId)) continue;
            visited.add(currentNodeId);

            for (MapLine edge : mapLineRepository.findAllByNodeFirst_NodeId(currentNodeId)) {
                double weight = edge.getWeightShortest();
                if(routeType.equals(routeTypeOptimal)){
                    double setOptimalWeight = (edge.getWeightOptimal()+ 90) / 1000;
                    weight += setOptimalWeight;
                }

                int neighborNodeId = edge.getNodeFirst().getNodeId() == currentNodeId ? edge.getNodeSecond().getNodeId() : edge.getNodeFirst().getNodeId();
                double distanceThroughCurrent = distances.getOrDefault(currentNodeId, Double.MAX_VALUE) + weight;

                if (distanceThroughCurrent < distances.getOrDefault(neighborNodeId, Double.MAX_VALUE)) {
                    distances.put(neighborNodeId, distanceThroughCurrent);
                    previousNodes.put(neighborNodeId, currentNodeId);
                    priorityQueue.offer(neighborNodeId);
                }
            }
        }

        log.info("Shortest Route : " + distances.get(arrivalsNodeId) + "m");

        // 경로 역추적
        routeBackTracking(departuresNodeId, arrivalsNodeId, nodeList, previousNodes, mapNodeRepository);
        int totalTime = aiService.calculateTime(nodeList, aIData);
        return NavigationResponseDTO.toNavigationResponseDTO(routeType, totalTime, nodeList);
    }

    public static void routeBackTracking(Integer departuresNodeId, Integer arrivalsNodeId, List<NavigationResponseDTO.NodeDTO> nodeList, Map<Integer, Integer> previousNodes, MapNodeRepository mapNodeRepository) {
        int currentNodeId = arrivalsNodeId;
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
        if(nodeList.isEmpty()) return;

        nodeList.add(NavigationResponseDTO.NodeDTO.builder()
                .nodeId(departuresNodeId)
                .latitude(mapNodeRepository.findByNodeId(departuresNodeId).getNodeLatitude())
                .longitude(mapNodeRepository.findByNodeId(departuresNodeId).getNodeLongitude())
                .altitude(mapNodeRepository.findByNodeId(departuresNodeId).getNodeAltitude())
                .build());
        Collections.reverse(nodeList);
    }
}








