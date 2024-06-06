package com.f2z.gach.History.Service;

import com.f2z.gach.AI.AiModel;
import com.f2z.gach.AI.AiModelRepository;
import com.f2z.gach.History.DTO.HistoryRequestDTO;
import com.f2z.gach.History.DTO.HistoryResponseDTO;
import com.f2z.gach.History.Entity.UserHistory;
import com.f2z.gach.History.Repository.HistoryLineTimeRepository;
import com.f2z.gach.History.Repository.UserHistoryRepository;
import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.Map.Repository.MapLineRepository;
import com.f2z.gach.Map.Repository.MapNodeRepository;
import com.f2z.gach.Response.ResponseEntity;
import com.f2z.gach.User.Repository.UserGuestRepository;
import com.f2z.gach.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HistoryServiceImpl implements HistoryService{
    private final UserHistoryRepository userHistoryRepository;
    private final UserRepository userRepository;
    private final UserGuestRepository userGuestRepository;
    private final MapNodeRepository mapNodeRepository;
    private final HistoryLineTimeRepository historyLineTimeRepository;
    private final MapLineRepository mapLineRepository;
    private final AiModelRepository aiModelRepository;

    @Override
    public ResponseEntity<List<HistoryResponseDTO.UserHistoryListStructure>> getHistoryList(Long userId) {
        List<UserHistory> userHistoryList= userHistoryRepository.findAllByUser_userId(userId);
        if(userHistoryList.isEmpty()){
            if(userRepository.existsByUserId(userId)){
                return ResponseEntity.saveButNoContent(null);
            }
            else{
                return ResponseEntity.notFound(null);
            }
        }else{
            return ResponseEntity.requestSuccess(HistoryResponseDTO.toUserHistoryListResponseDTO(userHistoryList));
        }
    }

    @Override
    public ResponseEntity<HistoryResponseDTO.respondSuccess> createHistory(HistoryRequestDTO.UserHistoryRequestDTO lineHistory) {
        UserHistory user = userHistoryRepository.save(HistoryRequestDTO.UserHistoryRequestDTO.toEntity(lineHistory, userRepository, userGuestRepository, mapNodeRepository));
        List<HistoryRequestDTO.HistoryLineTimeRequestDTO> lineTimeList = lineHistory.getTimeList();
        log.info("lineTimeList : " + lineTimeList.toString());

        if(lineTimeList.isEmpty()){
            return ResponseEntity.notFound(null);
        }

        for(HistoryRequestDTO.HistoryLineTimeRequestDTO lineTime : lineTimeList){
            if(lineTime.getFirstNodeId() == 0)continue;
            MapLine line = mapLineRepository.findByNodeFirstNodeIdAndNodeSecondNodeId(lineTime.getFirstNodeId(), lineTime.getSecondNodeId());
            Double Velocity = line.getWeightShortest()/lineTime.getTime();
            historyLineTimeRepository.save(HistoryRequestDTO.lineHistoryDTO.toEntity(user, line, lineTime.getTime(), Velocity));
        }

        Optional<AiModel> currentAiModel = aiModelRepository.findByIsCheckedTrue();
        log.info("currentAiModel : " + currentAiModel.toString());
        if(currentAiModel.isPresent()){
            AiModel aiModel = currentAiModel.get();
            aiModel.setCnt(aiModel.getCnt()+1);
            aiModel.setTotalSatisfaction(aiModel.getTotalSatisfaction() + lineHistory.getSatisfactionTime().ordinal() +1);
            log.info("AiModel에 만족도 정상적으로 저장");
            aiModelRepository.save(aiModel);
        }else log.info("aiModel is null : " + currentAiModel);

        return ResponseEntity.saveSuccess(HistoryResponseDTO.toRespondSuccess(user));
    }

    @Override
    public ResponseEntity<List<MapNode>> getTopThreeNode() {
        List<Object[]> results = userHistoryRepository.findTopMapNodes(3);
        log.info("results : " + Arrays.toString(results.get(0)));
        List<MapNode> topThreeNodes = results.stream().map(result -> mapNodeRepository.findByNodeId((Integer) result[0])).collect(Collectors.toList());
        return ResponseEntity.requestSuccess(topThreeNodes);
    }
}
