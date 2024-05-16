package com.f2z.gach.Map.Service;

import com.f2z.gach.Map.DTO.NavigationResponseDTO;
import com.f2z.gach.Map.DTO.PlaceResponseDTO;
import com.f2z.gach.Response.ResponseEntity;
import com.f2z.gach.Response.ResponseListEntity;

import java.util.List;

public interface MapService {
    ResponseEntity<PlaceResponseDTO.respondPlaceList> getBuildingInfoList();

    ResponseEntity<PlaceResponseDTO.toRespondBuildingInfo> getBuildingInfo(Integer placeId);

    ResponseEntity<List<PlaceResponseDTO.respondKeywordList>> getKeywordResult(String target);

    ResponseEntity<PlaceResponseDTO.placeLocationDTO> getKeywordDetailResult(Integer placeId);

    ResponseEntity<List<PlaceResponseDTO.placeLocationDTO>> getPlaceListByCategory(String placeCategory);

    ResponseListEntity<NavigationResponseDTO> getNowRoute(Integer placeId, Double latitude, Double longitude, Double altitude) throws Exception;

    ResponseListEntity<NavigationResponseDTO> getRoute(Integer departure, Integer arrival) throws Exception;
}
