package com.f2z.gach.Map.Service;

import com.f2z.gach.Map.DTO.PlaceResponseDTO;
import com.f2z.gach.Map.Entity.BuildingKeyword;
import com.f2z.gach.Response.ResponseEntity;

import java.util.List;

public interface MapService {
    ResponseEntity<PlaceResponseDTO.respondPlaceList> getBuildingInfoList();

    ResponseEntity<PlaceResponseDTO.toRespondBuildingInfo> getBuildingInfo(Integer placeId);

    ResponseEntity<List<BuildingKeyword>> getKeywordResult(String target);

    ResponseEntity<PlaceResponseDTO.BuildingKeywordResponseDTO> getKeywordDetailResult(Integer keywordId);
}
