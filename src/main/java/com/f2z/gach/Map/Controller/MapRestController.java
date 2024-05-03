package com.f2z.gach.Map.Controller;

import com.f2z.gach.Map.DTO.PlaceResponseDTO;
import com.f2z.gach.Map.Entity.BuildingKeyword;
import com.f2z.gach.Map.Service.MapService;
import com.f2z.gach.Response.ResponseEntity;
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
    public ResponseEntity<List<BuildingKeyword>> getKeywordResult(@RequestParam String target) throws Exception {
        return mapService.getKeywordResult(target);
    }

    @GetMapping("/find/{keywordId}")
    public ResponseEntity<PlaceResponseDTO.BuildingKeywordResponseDTO> getKeywordDetailResult(@PathVariable Integer keywordId) throws Exception {
        return mapService.getKeywordDetailResult(keywordId);
    }

}
