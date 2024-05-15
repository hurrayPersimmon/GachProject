package com.f2z.gach.AI;

import com.f2z.gach.DataGetter.dataEntity;
import com.f2z.gach.DataGetter.dataRepository;
import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Repository.MapLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {
    private final dataRepository dataRepo;
    private final MapLineRepository lineRepo;

    // 현재 모든 데이터
    public List<dataEntity> getData(){
        List<dataEntity> list = dataRepo.findAll();
        return list;
    }

    // 현재의 데이터를 기반으로 필터링 작업 시작
    public List<dataEntity> filterData(int min, int max, boolean mapping) {
        List<dataEntity> originalList = dataRepo.findAll();
        List<MapLine> lines = lineRepo.findAll();
        List<dataEntity> filteredList = new ArrayList<>();

        originalList.stream()
                .filter(data -> data.getTakeTime() > min && data.getTakeTime() < max)
                .forEach(data -> {
                    if (mapping) {
                        lines.stream()
                                .filter(line -> ((data.getNode1() == line.getNodeFirst().getNodeName()) || (data.getNode1() == line.getNodeSecond().getNodeName())) &&
                                        ((data.getNode2() == line.getNodeFirst().getNodeName()) || (data.getNode2() == line.getNodeSecond().getNodeName())))
                                .forEach(
                                        line -> filteredList.add(data));
                    } else {
                        filteredList.add(data); // "mapping" 값이 false 일 때는 조건 필터링을 하지 않고 모든 데이터를 추가합니다.
                    }
                });
        log.info(filteredList.toString());
        return filteredList;
    }

}
