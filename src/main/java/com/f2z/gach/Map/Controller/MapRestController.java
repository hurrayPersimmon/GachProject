package com.f2z.gach.Map.Controller;

import com.f2z.gach.Map.DTO.NavigationResponseDTO;
import com.f2z.gach.Map.DTO.PlaceResponseDTO;
import com.f2z.gach.Map.Service.MapService;
import com.f2z.gach.Response.ResponseEntity;
import com.f2z.gach.Response.ResponseListEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapRestController {
    private final MapService mapService;

    @GetMapping("/building-info/list")
    public ResponseEntity<PlaceResponseDTO.respondPlaceList> getBuildingInfoList() throws Exception {
        return mapService.getBuildingInfoList();
    }

    @GetMapping("/building-floor/{placeId}")
    public ResponseEntity<PlaceResponseDTO.toRespondBuildingInfo> getBuildingInfo(@PathVariable Integer placeId) throws Exception {
        return mapService.getBuildingInfo(placeId);
    }

    @GetMapping("/find")
    public ResponseEntity<List<PlaceResponseDTO.respondKeywordList>> getKeywordResult(@RequestParam String target) throws Exception {
        return mapService.getKeywordResult(target);
    }

    @GetMapping("/find/{placeId}")
    public ResponseEntity<PlaceResponseDTO.placeLocationDTO> getKeywordDetailResult(@PathVariable Integer placeId) throws Exception {
        return mapService.getKeywordDetailResult(placeId);
    }

    @GetMapping("/{placeCategory}")
    public ResponseEntity<List<PlaceResponseDTO.placeLocationDTO>> getPlaceListByCategory(@PathVariable String placeCategory) throws Exception {
        return mapService.getPlaceListByCategory(placeCategory);
    }

    @GetMapping("/route-now/{placeId}")
    public ResponseListEntity<NavigationResponseDTO> getNowRoute(@PathVariable Integer placeId,
                                                              @RequestParam Double latitude,
                                                              @RequestParam Double longitude,
                                                              @RequestParam Double altitude) throws Exception {
        return mapService.getNowRoute(placeId, latitude, longitude, altitude);
    }

    @GetMapping("/route")
    public ResponseListEntity<NavigationResponseDTO> getRoute(@RequestParam Integer departures, @RequestParam Integer arrivals){
        return mapService.getRoute(departures, arrivals);
    }



}
