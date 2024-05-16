package com.f2z.gach.Map.Service;

import com.f2z.gach.EnumType.College;
import com.f2z.gach.EnumType.Departments;
import com.f2z.gach.EnumType.PlaceCategory;
import com.f2z.gach.Map.BusLine;
import com.f2z.gach.Map.DTO.NavigationResponseDTO;
import com.f2z.gach.Map.DTO.PlaceResponseDTO;
import com.f2z.gach.Map.Entity.*;
import com.f2z.gach.Map.Repository.*;
import com.f2z.gach.Response.ResponseEntity;
import com.f2z.gach.Response.ResponseListEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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

    @Override
    public ResponseListEntity<NavigationResponseDTO> getNowRoute(Integer placeId, Double latitude, Double longitude, Double altitude) throws Exception {
        MapNode departures = MapNode.builder()
                .nodeId(0)
                .nodeLatitude(latitude)
                .nodeLongitude(longitude)
                .nodeAltitude(altitude)
                .build();
        log.info("restDep"+ departures.toString());
        NavigationResponseDTO busRoute = getBusRoute(0, placeId, departures);
        Integer departuresId = getNearestNodeId(latitude, longitude, altitude);
        PlaceSource arrivalsPlace = placeSourceRepository.findByPlaceId(placeId);
        Integer arrivalsId = getNearestNodeId(arrivalsPlace.getPlaceLatitude(),
                                arrivalsPlace.getPlaceLongitude(),
                                arrivalsPlace.getPlaceAltitude());

        return getNavigationResponseDTOResponseListEntity(departuresId, arrivalsId, busRoute);
    }



    @Override
    public ResponseListEntity<NavigationResponseDTO> getRoute(Integer departures, Integer arrivals) throws Exception {
        NavigationResponseDTO busRoute = getBusRoute(departures, arrivals, null);

        PlaceSource departuresPlace = placeSourceRepository.findByPlaceId(departures);
        departures = getNearestNodeId(departuresPlace.getPlaceLatitude(),
                departuresPlace.getPlaceLongitude(),
                departuresPlace.getPlaceAltitude());

        PlaceSource arrivalsPlace = placeSourceRepository.findByPlaceId(arrivals);
        arrivals = getNearestNodeId(arrivalsPlace.getPlaceLatitude(),
                arrivalsPlace.getPlaceLongitude(),
                arrivalsPlace.getPlaceAltitude());


        return getNavigationResponseDTOResponseListEntity(departures, arrivals, busRoute);
    }

    private NavigationResponseDTO getBusRoute(Integer departures, Integer arrivals, MapNode departuresNode) throws Exception {
        List<BusLine.Node> busLine;
        if(departuresNode != null){
            busLine = BusLine.getBusLine(departuresNode, MapNode.toRouteEntity(placeSourceRepository.findByPlaceId(arrivals)));
        }else{
            busLine = BusLine.getBusLine(MapNode.toRouteEntity(placeSourceRepository.findByPlaceId(departures)),
                    MapNode.toRouteEntity(placeSourceRepository.findByPlaceId(arrivals)));
        }
        if(busLine == null|| busLine.isEmpty()) return null;
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
            log.info(busLine.toString());
            log.info(nodeList.toString());
            int tailIndex = nodeList.size() -1;
            NavigationResponseDTO gettingOffRoute = calculateRoute(routeBus, getNearestNodeId(nodeList.get(tailIndex).getLatitude(), nodeList.get(tailIndex).getLongitude(), null), arrivals);
            List<NavigationResponseDTO.NodeDTO> busRouteMergedlist = new ArrayList<>();
            busRouteMergedlist.addAll(nodeList);
            busRouteMergedlist.addAll(gettingOffRoute.getNodeList());
            NavigationResponseDTO busMergedRoute = NavigationResponseDTO.toNavigationResponseDTO(routeBus, 0, busRouteMergedlist);
            return busMergedRoute;
        }
    }

    private ResponseListEntity<NavigationResponseDTO> getNavigationResponseDTOResponseListEntity(Integer departures, Integer arrivals, NavigationResponseDTO busRoute) {
        if(departures == 0 || arrivals == 0 || departures == null || arrivals == null){
            return ResponseListEntity.notFound(null);
        }
        if(Objects.equals(departures, arrivals)){
            return ResponseListEntity.sameNode(null);
        }

        NavigationResponseDTO shortestRoute  = calculateRoute(routeTypeShortest, departures, arrivals);
        NavigationResponseDTO optimalRoute = calculateRoute(routeTypeOptimal, departures, arrivals);

        List<NavigationResponseDTO> routes;
        if(busRoute == null) routes = Arrays.asList(shortestRoute, optimalRoute);
        else routes = Arrays.asList(shortestRoute, optimalRoute, busRoute);

        return ResponseListEntity.requestListSuccess(routes.toArray(new NavigationResponseDTO[0]));
    }


    private Integer getNearestNodeId(Double placeLatitude, Double placeLongitude, Double placeAltitude) {
        int resultId = 0;
        final int R = 6371; // 지구의 반지름 (킬로미터)

        double minDistance= Double.MAX_VALUE;
        List<MapNode> nodeList = mapLineRepository.findAll().stream().map(MapLine::getNodeFirst).toList();
        for(MapNode node : nodeList){
            double distance;
            if(placeAltitude == null) distance = getHaversine(placeLatitude, placeLongitude, node);
            else distance = getVincenty(placeLatitude, placeLongitude, placeAltitude, node);

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

    private NavigationResponseDTO calculateRoute(String routeType, Integer departuresId, Integer arrivalsId) {
        List<NavigationResponseDTO.NodeDTO> nodeList = new ArrayList<>();
        Map<Integer, Double> distances = new HashMap<>();
        Map<Integer, Integer> previousNodes = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        distances.put(departuresId, 0.0);
        priorityQueue.offer(departuresId);

        while (!priorityQueue.isEmpty()) {
            int currentNodeId = priorityQueue.poll();

            if (currentNodeId == arrivalsId) {
                break;
            }

            if (visited.contains(currentNodeId)) {
                continue;
            }

            visited.add(currentNodeId);

            for (MapLine edge : mapLineRepository.findAllByNodeFirst_NodeId(currentNodeId)) {
                double weight = routeType.equals(routeTypeOptimal) ?  edge.getWeightOptimal() + 180 : edge.getWeightShortest();
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
        routeBackTracking(departuresId, arrivalsId, nodeList, previousNodes, mapNodeRepository);

        return NavigationResponseDTO.toNavigationResponseDTO(routeType, 0, nodeList);
    }

    public static void routeBackTracking(Integer departuresId, Integer arrivalsId, List<NavigationResponseDTO.NodeDTO> nodeList, Map<Integer, Integer> previousNodes, MapNodeRepository mapNodeRepository) {
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
    }
}








