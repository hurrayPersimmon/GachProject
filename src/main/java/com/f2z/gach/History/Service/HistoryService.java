package com.f2z.gach.History.Service;

import com.f2z.gach.History.DTO.HistoryRequestDTO;
import com.f2z.gach.History.DTO.HistoryResponseDTO;
import com.f2z.gach.History.Entity.UserHistory;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.Response.ResponseEntity;

import java.util.List;

public interface HistoryService {
    ResponseEntity<List<HistoryResponseDTO.UserHistoryListStructure>> getHistoryList(Long userId);

    ResponseEntity<HistoryResponseDTO.respondSuccess> createHistory(HistoryRequestDTO.UserHistoryRequestDTO lineHistory);

    ResponseEntity<List<MapNode>> getTopThreeNode();
}
