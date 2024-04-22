package com.f2z.gach.Map.Service;

import com.f2z.gach.EnumType.PlaceCategory;
import com.f2z.gach.Map.DTO.PlaceResponseDTO;
import com.f2z.gach.Map.Entity.BuildingFloor;
import com.f2z.gach.Map.Entity.PlaceSource;
import com.f2z.gach.Map.Repository.BuildingFloorRepository;
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
}
