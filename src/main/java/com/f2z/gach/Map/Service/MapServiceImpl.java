package com.f2z.gach.Map.Service;

import com.f2z.gach.EnumType.College;
import com.f2z.gach.EnumType.Departments;
import com.f2z.gach.EnumType.PlaceCategory;
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
            List<BuildingFloor> buildingFloors= buildingFloorRepository.findAllByBuildingCode(placeId);
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
    public ResponseListEntity<NavigationResponseDTO> getNowRoute(Integer placeId, Double latitude, Double longitude, Double altitude) {
        Integer departureId = getNearestNodeId(latitude, longitude, altitude);
        PlaceSource arrivalPlace = placeSourceRepository.findByPlaceId(placeId);
        Integer arrivalId = getNearestNodeId(arrivalPlace.getPlaceLatitude(),
                                arrivalPlace.getPlaceLongitude(),
                                arrivalPlace.getPlaceAltitude());

        return getNavigationResponseDTOResponseListEntity(departureId, arrivalId);
    }



    @Override
    public ResponseListEntity<NavigationResponseDTO> getRoute(Integer departure, Integer arrival) {
        PlaceSource departurePlace = placeSourceRepository.findByPlaceId(departure);
        departure = getNearestNodeId(departurePlace.getPlaceLatitude(),
                departurePlace.getPlaceLongitude(),
                departurePlace.getPlaceAltitude());

        PlaceSource arrivalPlace = placeSourceRepository.findByPlaceId(arrival);
        arrival = getNearestNodeId(arrivalPlace.getPlaceLatitude(),
                arrivalPlace.getPlaceLongitude(),
                arrivalPlace.getPlaceAltitude());

        return getNavigationResponseDTOResponseListEntity(departure, arrival);
    }

    private ResponseListEntity<NavigationResponseDTO> getNavigationResponseDTOResponseListEntity(Integer departure, Integer arrival) {
        if(departure == 0 || arrival == 0 || departure == null || arrival == null){
            return ResponseListEntity.notFound(null);
        }
        if(Objects.equals(departure, arrival)){
            return ResponseListEntity.sameNode(null);
        }

        NavigationResponseDTO shortestRoute  = calculateRoute(routeTypeShortest, departure, arrival);
        NavigationResponseDTO optimalRoute = calculateRoute(routeTypeOptimal, departure, arrival);
        List<NavigationResponseDTO> routes = Arrays.asList(shortestRoute, optimalRoute);

        return ResponseListEntity.requestListSuccess(routes.toArray(new NavigationResponseDTO[0]));
    }


    private Integer getNearestNodeId(Double placeLatitude, Double placeLongitude, Double placeAltitude) {
        int resultId = 0;
        double minDistance= Double.MAX_VALUE;
        List<MapNode> nodeList = mapLineRepository.findAll().stream().map(MapLine::getNodeFirst).toList();
        for(MapNode node : nodeList){
            Double distance = Math.sqrt(Math.pow(node.getNodeLatitude()-placeLatitude, 2)
                        + Math.pow(node.getNodeLongitude()-placeLongitude, 2)
                        + Math.pow(node.getNodeAltitude()-placeAltitude, 2));
            if(minDistance > distance){
                minDistance = distance;
                resultId = node.getNodeId();
            }
        }
        return resultId;
    }

    private NavigationResponseDTO calculateRoute(String routeType, Integer departureId, Integer arrivalId) {
        List<NavigationResponseDTO.NodeDTO> nodeList = new ArrayList<>();
        Map<Integer, Double> distances = new HashMap<>();
        Map<Integer, Integer> previousNodes = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        distances.put(departureId, 0.0);
        priorityQueue.offer(departureId);

        while (!priorityQueue.isEmpty()) {
            int currentNodeId = priorityQueue.poll();

            if (currentNodeId == arrivalId) {
                break;
            }

            if (visited.contains(currentNodeId)) {
                continue;
            }

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
        int currentNodeId = arrivalId;
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
                .nodeId(departureId)
                .latitude(mapNodeRepository.findByNodeId(departureId).getNodeLatitude())
                .longitude(mapNodeRepository.findByNodeId(departureId).getNodeLongitude())
                .altitude(mapNodeRepository.findByNodeId(departureId).getNodeAltitude())
                .build());
        Collections.reverse(nodeList);

        return NavigationResponseDTO.toNavigationResponseDTO(routeType, 0, nodeList);
    }
}








