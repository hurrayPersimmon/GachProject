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
    public List<dataEntity> filterData(int min, int max) {
        List<dataEntity> originalList = dataRepo.findAll();
        List<dataEntity> filteredList = new ArrayList<>();

        originalList.stream()
                .filter(data -> data.getTakeTime() != null && data.getTakeTime() > (double)min && data.getTakeTime() < (double)max)
                .forEach(data -> {
                    filteredList.add(data);
                });
        log.info(filteredList.toString());
        return filteredList;
    }

}
