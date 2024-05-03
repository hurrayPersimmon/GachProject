package com.f2z.gach.History.Service;

import com.f2z.gach.History.DTO.HistoryResponseDTO;
import com.f2z.gach.Response.ResponseEntity;

public interface HistoryService {
    ResponseEntity<HistoryResponseDTO.UserHistoryList> getHistoryList(Integer page, Long userId);
}
