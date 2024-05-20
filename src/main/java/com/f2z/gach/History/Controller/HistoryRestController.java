package com.f2z.gach.History.Controller;

import com.f2z.gach.History.DTO.HistoryRequestDTO;
import com.f2z.gach.History.DTO.HistoryResponseDTO;
import com.f2z.gach.History.Entity.UserHistory;
import com.f2z.gach.History.Service.HistoryService;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.Response.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryRestController {
    private final HistoryService historyService;

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<HistoryResponseDTO.UserHistoryListStructure>> getHistoryList(@PathVariable Long userId) throws Exception {
        return historyService.getHistoryList(userId);
    }

    @PostMapping()
    public ResponseEntity<HistoryResponseDTO.respondSuccess> createHistory(@RequestBody HistoryRequestDTO.UserHistoryRequestDTO lineHistory) {
        return historyService.createHistory(lineHistory);
    }

    @GetMapping("/top-nodes")
    public ResponseEntity<List<MapNode>> getTopThreeNode() {
        return historyService.getTopThreeNode();
    }



}
