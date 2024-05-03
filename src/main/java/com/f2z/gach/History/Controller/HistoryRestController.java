package com.f2z.gach.History.Controller;

import com.f2z.gach.History.DTO.HistoryResponseDTO;
import com.f2z.gach.History.Service.HistoryService;
import com.f2z.gach.Response.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryRestController {
    private final HistoryService historyService;

    @GetMapping("/list/{userId}")
    public ResponseEntity<HistoryResponseDTO.UserHistoryList> getHistoryList(@RequestParam Integer page, @PathVariable Long userId) throws Exception {
        return historyService.getHistoryList(page, userId);
    }



}
