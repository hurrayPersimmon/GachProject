package com.f2z.gach.Map;

import com.f2z.gach.Map.DTOs.Responses.PlaceResponseDTO;
import com.f2z.gach.Map.Entities.BuildingInfo;
import com.f2z.gach.Map.Entities.PlaceSource;
import com.f2z.gach.Response.ResponseEntity;

import java.util.List;

public interface MapService {
    ResponseEntity<PlaceResponseDTO.respondPlaceList> getBuildingInfoList();

    ResponseEntity<PlaceResponseDTO.toRespondBuildingInfo> getBuildingInfo(Integer placeId);
}
