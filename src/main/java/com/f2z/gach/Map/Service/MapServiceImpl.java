package com.f2z.gach.Map.Service;

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

import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class MapServiceImpl implements MapService{
    private final PlaceSourceRepository placeSourceRepository;
    private final BuildingFloorRepository buildingFloorRepository;
    private final BuildingKeywordRepository buildingKeywordRepository;


    public List<BuildingKeyword> getBuildingInfoListByKeyword(String target){
        if(!buildingKeywordRepository.findAllByBuildingNameContaining(target).isEmpty())
            return buildingKeywordRepository.findAllByBuildingNameContaining(target);
        if(!buildingKeywordRepository.findAllByDepartmentContaining(target).isEmpty())
            return buildingKeywordRepository.findAllByDepartmentContaining(target);
        if(!buildingKeywordRepository.findAllByDepartmentContaining(target).isEmpty())
            return buildingKeywordRepository.findAllByDepartmentContaining(target);
        if(!buildingKeywordRepository.findAllByCollegeContaining(target).isEmpty())
            return buildingKeywordRepository.findAllByCollegeContaining(target);
        else return null;
    }

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
    public ResponseEntity<List<BuildingKeyword>> getKeywordResult(String target) {
        List<BuildingKeyword> buildingInfoList = getBuildingInfoListByKeyword(target);
        if(buildingInfoList != null) {
            return ResponseEntity.requestSuccess(buildingInfoList);
        }else{
            return ResponseEntity.notFound(null);
        }
    }

    @Override
    public ResponseEntity<PlaceResponseDTO.BuildingKeywordResponseDTO> getKeywordDetailResult(Integer keywordId) {
        BuildingKeyword buildingKeyword = buildingKeywordRepository.findByKeywordId(keywordId);
        PlaceSource place = placeSourceRepository.findByPlaceId(buildingKeyword.getBuildingCode());
        return ResponseEntity.requestSuccess(PlaceResponseDTO.toBuildingKeywordResponseDTO(buildingKeyword, place));
    }



}
