package com.f2z.gach.Map.Service;

import com.f2z.gach.Map.DTO.PlaceResponseDTO;
import com.f2z.gach.Response.ResponseEntity;

public interface MapService {
    ResponseEntity<PlaceResponseDTO.respondPlaceList> getBuildingInfoList();

    ResponseEntity<PlaceResponseDTO.toRespondBuildingInfo> getBuildingInfo(Integer placeId);
}
