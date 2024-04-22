package com.f2z.gach.Map.Controller;

import com.f2z.gach.Map.DTO.PlaceResponseDTO;
import com.f2z.gach.Map.Service.MapService;
import com.f2z.gach.Response.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {
    private final MapService mapService;

    @GetMapping("/building-info/list")
    public ResponseEntity<PlaceResponseDTO.respondPlaceList> getBuildingInfoList() throws Exception {
        return mapService.getBuildingInfoList();
    }

    @GetMapping("/building-floor/{placeId}")
    public ResponseEntity<PlaceResponseDTO.toRespondBuildingInfo> getBuildingInfo(@PathVariable Integer placeId) throws Exception {
        return mapService.getBuildingInfo(placeId);
    }

}
