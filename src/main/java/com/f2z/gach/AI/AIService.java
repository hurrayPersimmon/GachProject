package com.f2z.gach.AI;

import com.f2z.gach.DataGetter.dataEntity;
import com.f2z.gach.DataGetter.dataRepository;
import com.f2z.gach.History.Entity.HistoryLineTime;
import com.f2z.gach.History.Repository.HistoryLineTimeRepository;
import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Entity.MapNode;
import com.f2z.gach.Map.Repository.MapLineRepository;
import com.f2z.gach.User.Entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {
    private final AiModelRepository aiRepo;
    private final HistoryLineTimeRepository lineTimeRepo;
    private final MapLineRepository mapLineRepository;
    private ProcessBuilder processBuilder;
    final String localPythonPath = "/opt/anaconda3/bin/python3";
    final String localModelPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Python/lstm.py";
    final String localSaveSHPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Util/save.sh";
    final String localOutPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Python/output.py";
    final String localReModelPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Python/re_learn.py";
    final String csvFilePath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Data/data.csv";
    final String localDeleteSHPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Util/delete.sh";
    final String tempOutputPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Python/tree_output.py";
//    final String localPythonPath = "python3";
//    final String localModelPath = "/home/t24102/GachProject/AI/Python/lstm.py";
//    final String localSaveSHPath = "/home/t24102/GachProject/AI/Util/save.sh";
//    final String localOutPath = "/home/t24102/GachProject/AI/Python/output.py";
//    final String localReModelPath = "/home/t24102/GachProject/AI/Python/re_learn.py";
//    final String csvFilePath = "/home/t24102/GachProject/AI/Data/data.csv";
//    final String localDeleteSHPath = "/home/t24102/GachProject/AI/Util/delete.sh";

    // 현재의 데이터를 기반으로 필터링 작업 시작
    public int filterData(int min, int max) {
        List<HistoryLineTime> originalList = lineTimeRepo.findAll();
        List<dataEntity> filteredList = new ArrayList<>();

        originalList.stream()
                .filter(data -> data.getLineTime() != null && data.getLineTime() > (double)min && data.getLineTime() < (double)max)
                .forEach(data -> filteredList.add(dataEntity.parseHistory(data)));

        String csvFile = csvFilePath;

        try (FileWriter writer = new FileWriter(csvFile)) {
            // CSV 파일 헤더 쓰기
            writer.append("birthYear,gender,height,weight,walkSpeed,temperature,precipitationProbability,precipitation,weightShortest,weightOptimal,takeTime\n");

            // 데이터 쓰기
            for (dataEntity data : filteredList) {
                writer.append(String.valueOf(data.getBirthYear())).append(",");
                writer.append(String.valueOf(data.getGender())).append(",");
                writer.append(String.valueOf(data.getHeight())).append(",");
                writer.append(String.valueOf(data.getWeight())).append(",");
                writer.append(String.valueOf(data.getWalkSpeed())).append(",");
                writer.append(String.valueOf(data.getTemperature())).append(",");
                writer.append(String.valueOf(data.getPrecipitationProbability())).append(",");
                writer.append(String.valueOf(data.getPrecipitation())).append(",");
                writer.append(String.valueOf(data.getWeightOptimal())).append(",");
                writer.append(String.valueOf(data.getWeightShortest())).append(",");
                writer.append(String.valueOf(data.getTakeTime())).append("\n");
            }

            log.info("CSV 파일이 성공적으로 생성되었습니다.");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return filteredList.size();
    }

    public void saveModel(String modelName){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(localSaveSHPath, modelName);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            log.info("저장 완료 코드 : " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 재학습 모델 추가 데이터를 구분하는 작업만 하면 된다. 즉 인덱스 작업만 진행하면 됨.
    public String reLearnModel() throws Exception{
        processBuilder = new ProcessBuilder(localPythonPath, localReModelPath);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        StringBuilder sb = new StringBuilder();
        log.info("재학습 시작");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
            if(line.startsWith("Validation")) {
                reader.close();
                return sb.toString();
            }
        }
        reader.close();
        return sb.toString();
    }

    public int calculateTime(List<MapNode> list, User user){
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/3);

        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        for(int i = 0; i < list.size()-1; i++){
            MapLine shortMapLine = mapLineRepository.findLineIdByNodeFirst_NodeIdAndNodeSecond_NodeId(shortList.get(i).getNodeId(), shortList.get(i+1).getNodeId());
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return modelOutput(shortMapLine, data);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, executor);
            futures.add(future);
        }

        return futures.stream()
                .map(CompletableFuture::join)
                .reduce(0, Integer::sum);
    }

    public Integer modelOutput(MapLine line, dataEntity data) throws Exception{
        processBuilder = new ProcessBuilder(localPythonPath, tempOutputPath,
                String.valueOf(data.getBirthYear()), String.valueOf(data.getGender()),
                String.valueOf(data.getHeight()), String.valueOf(data.getWeight()),
                String.valueOf(data.getWalkSpeed()), String.valueOf(data.getTemperature()),
                String.valueOf(data.getPrecipitationProbability()), String.valueOf(data.getPrecipitation()),
                String.valueOf(line.getWeightShortest()), String.valueOf(line.getWeightOptimal()));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        String takeTime = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String printLine;
            while ((printLine = reader.readLine()) != null) {
                takeTime = printLine;
                log.info(takeTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        process.destroy();

        takeTime = takeTime.substring(1, takeTime.length() - 1);

        return Integer.parseInt(takeTime);
    }

    public void deleteModel(AiModel aiModel) {
        try {
            log.info(aiModel.getAiModelPath());
            processBuilder = new ProcessBuilder(localDeleteSHPath, aiModel.getAiModelName());
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            log.info("삭제 완료 코드 : " + exitCode);
            process.destroy();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
