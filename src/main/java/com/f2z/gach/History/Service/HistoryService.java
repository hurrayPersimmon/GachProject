package com.f2z.gach.History.Service;

import com.f2z.gach.History.DTO.HistoryResponseDTO;
import com.f2z.gach.History.Entity.UserHistory;
import com.f2z.gach.Response.ResponseEntity;

import java.util.List;

public interface HistoryService {
    ResponseEntity<List<UserHistory>> getHistoryList(Long userId);
}
