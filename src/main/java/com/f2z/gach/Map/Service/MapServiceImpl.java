package com.f2z.gach.Map.Service;

import com.f2z.gach.EnumType.College;
import com.f2z.gach.EnumType.Departments;
import com.f2z.gach.EnumType.PlaceCategory;
import com.f2z.gach.Map.DTO.PlaceResponseDTO;
import com.f2z.gach.Map.Entity.BuildingFloor;
import com.f2z.gach.Map.Entity.BuildingKeyword;
import com.f2z.gach.Map.Entity.PlaceSource;
import com.f2z.gach.Map.Repository.BuildingFloorRepository;
import com.f2z.gach.Map.Repository.BuildingKeywordRepository;
import com.f2z.gach.Map.Repository.PlaceSourceRepository;
import com.f2z.gach.Response.ResponseEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class MapServiceImpl implements MapService{
    private final PlaceSourceRepository placeSourceRepository;
    private final BuildingFloorRepository buildingFloorRepository;
    private final BuildingKeywordRepository buildingKeywordRepository;




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


}
