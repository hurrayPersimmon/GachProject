package com.f2z.gach.AI;

import com.f2z.gach.DataGetter.dataEntity;
import com.f2z.gach.DataGetter.dataRepository;
import com.f2z.gach.History.Entity.HistoryLineTime;
import com.f2z.gach.History.Repository.HistoryLineTimeRepository;
import com.f2z.gach.Map.Entity.MapLine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {
    private final dataRepository dataRepo;
    private final HistoryLineTimeRepository lineTimeRepo;
    private ProcessBuilder processBuilder;
//    final String localPythonPath = "/opt/anaconda3/bin/python3";
//    final String localModelPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Python/lstm.py";
//    final String localSaveSHPath = "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Util/save.sh";

    final String localPythonPath = "python3";
    final String localModelPath = "/home/t24102/GachProject/AI/Python/lstm.py";
    final String localSaveSHPath = "/home/t24102/GachProject/AI/Util/save.sh";
    final String localOutPath = "/home/t24102/GachProject/AI/Python/output.sh";

    // 현재 모든 데이터
    public List<dataEntity> getData(){
        return dataRepo.findAll();
    }

    // 현재의 데이터를 기반으로 필터링 작업 시작
    public int filterData(int min, int max) {
        List<dataEntity> originalList = dataRepo.findAll();
        List<dataEntity> filteredList = new ArrayList<>();
        List<HistoryLineTime> lineTimeList = lineTimeRepo.findAll();

        lineTimeList.forEach( i -> {
            dataEntity data = dataEntity.parseHistory(i);
            originalList.add(data);
        });

        originalList.stream()
                .filter(data -> data.getTakeTime() != null && data.getTakeTime() > (double)min && data.getTakeTime() < (double)max)
                .forEach(filteredList::add);

        String csvFile = "/home/t24102/GachProject/AI/Data/data.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            // CSV 파일 헤더 쓰기
            writer.append("dataId,node1,node2,birthYear,gender,height,weight,walkSpeed,temperature,precipitationProbability,precipitation,takeTime,weightShortest,weightOptimal\n");

            // 데이터 쓰기
            for (dataEntity data : filteredList) {
                writer.append(String.valueOf(data.getDataId())).append(",");
                writer.append(data.getNode1()).append(",");
                writer.append(data.getNode2()).append(",");
                writer.append(String.valueOf(data.getBirthYear())).append(",");
                writer.append(String.valueOf(data.getGender())).append(",");
                writer.append(String.valueOf(data.getHeight())).append(",");
                writer.append(String.valueOf(data.getWeight())).append(",");
                writer.append(String.valueOf(data.getWalkSpeed())).append(",");
                writer.append(String.valueOf(data.getTemperature())).append(",");
                writer.append(String.valueOf(data.getPrecipitationProbability())).append(",");
                writer.append(String.valueOf(data.getPrecipitation())).append(",");
                writer.append(String.valueOf(data.getTakeTime())).append(",");
                writer.append(String.valueOf(data.getWeightShortest())).append(",");
                writer.append(String.valueOf(data.getWeightOptimal())).append("\n");
            }

            log.info("CSV 파일이 성공적으로 생성되었습니다.");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return filteredList.size();
    }

    // 이것은 학습 데이터
    public String makeModel(int hidden, int epochs, int layers, double learningRate, int batch_size) throws Exception{
        processBuilder = new ProcessBuilder(localPythonPath, localModelPath,
                Integer.toString(hidden),
                Integer.toString(epochs),
                Integer.toString(layers),
                Double.toString(learningRate),
                Integer.toString(batch_size));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
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

    // 재학습 모델
    public void doMakeModel(int epochs, double learningRate) throws Exception{
        processBuilder = new ProcessBuilder(localPythonPath,
                "re_learn.py",
                Integer.toString(epochs),
                Double.toString(learningRate));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        log.info("재학습 시작");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            log.info(">>>  " + line); // 표준출력에 쓴다
        }
        reader.close();
    }

    public double modelOutput(MapLine line, dataEntity data) throws Exception{
        processBuilder = new ProcessBuilder(localPythonPath,
                localOutPath, String.valueOf(data.getBirthYear()), String.valueOf(data.getGender()),
                String.valueOf(data.getHeight()), String.valueOf(data.getWeight()),
                String.valueOf(data.getWalkSpeed()), String.valueOf(data.getTemperature()),
                String.valueOf(data.getPrecipitationProbability()), String.valueOf(data.getPrecipitation()),
                String.valueOf(line.getWeightShortest()),
                String.valueOf(line.getWeightOptimal()));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        String takeTime = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String printLine;
            while ((printLine = reader.readLine()) != null) {
                takeTime = printLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        process.destroy();

        int startIndex = takeTime.indexOf("[[") + 2;
        int endIndex = takeTime.indexOf("]]", startIndex);

        String numberString = takeTime.substring(startIndex, endIndex);
        return Double.parseDouble(numberString);
    }

}
