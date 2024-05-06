package com.f2z.gach.History.Service;

import com.f2z.gach.History.DTO.HistoryResponseDTO;
import com.f2z.gach.History.Entity.UserHistory;
import com.f2z.gach.History.Repository.UserHistoryRepository;
import com.f2z.gach.Response.ResponseEntity;
import com.f2z.gach.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HistoryServiceImpl implements HistoryService{
    private final UserHistoryRepository userHistoryRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<List<UserHistory>> getHistoryList( Long userId) {
        List<UserHistory> userHistoryList= userHistoryRepository.findAllByUserId(userId);
        if(userHistoryList.isEmpty()){
            if(userRepository.existsByUserId(userId)){
                return ResponseEntity.saveButNoContent(null);
            }
            else{
                return ResponseEntity.notFound(null);
            }
        }else{
            return ResponseEntity.requestSuccess(userHistoryList);
        }
    }
}
