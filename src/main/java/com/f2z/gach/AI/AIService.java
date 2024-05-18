package com.f2z.gach.AI;

import com.f2z.gach.DataGetter.dataEntity;
import com.f2z.gach.DataGetter.dataRepository;
import com.f2z.gach.Map.Entity.MapLine;
import com.f2z.gach.Map.Repository.MapLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIService {
    private final dataRepository dataRepo;
    private final MapLineRepository lineRepo;
    private ProcessBuilder processBuilder;

    // 현재 모든 데이터
    public List<dataEntity> getData(){
        List<dataEntity> list = dataRepo.findAll();
        return list;
    }

    // 현재의 데이터를 기반으로 필터링 작업 시작
    public int filterData(int min, int max) {
        List<dataEntity> originalList = dataRepo.findAll();
        List<dataEntity> filteredList = new ArrayList<>();

        originalList.stream()
                .filter(data -> data.getTakeTime() != null && data.getTakeTime() > (double)min && data.getTakeTime() < (double)max)
                .forEach(data -> {
                    filteredList.add(data);
                });
        log.info(filteredList.toString());
        String csvFile = "data.csv";

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

    // 현재 데이터와 학습 이후의 데이터를 어떻게 구분할 것인가?

    // 이것은 학습 데이터
    public int makeModel(int hidden, int epochs, int layers, double learningRate) throws Exception{
        processBuilder = new ProcessBuilder("/opt/anaconda3/bin/python3",
                "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Python/lstm.py",
                Integer.toString(hidden),
                Integer.toString(epochs),
                Integer.toString(layers),
                Double.toString(learningRate));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        log.info("학습 시작");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            log.info(">>>  " + line);
            if(line.startsWith("Validation")) return 1;
        }
        reader.close();
        return 0;

        //학습완료 후 엔티티로 어떻게 다룰 것인지?
    }

    // 재학습 모델
    public void doMakeModel(int epochs, double learningRate) throws Exception{
        processBuilder = new ProcessBuilder("/opt/anaconda3/bin/python3",
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
    int i = 1;
    // 결과 확인
    public double modelOutput(MapLine line, dataEntity data) throws Exception{
        log.info("호출 횟수 : " + i++);
        processBuilder = new ProcessBuilder("/opt/anaconda3/bin/python3",
                "/Users/nomyeongjun/Documents/2024-1/Project/GachProject/AI/Python/output.py", String.valueOf(data.getBirthYear()), String.valueOf(data.getGender()),
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
        // 숫자가 끝나는 인덱스 찾기
        int endIndex = takeTime.indexOf("]]", startIndex);

        // 숫자가 있는 부분 추출
        String numberString = takeTime.substring(startIndex, endIndex);
        log.info(numberString);
        // 추출한 문자열을 double로 변환하여 반환
        return Double.parseDouble(numberString);
    }

}
