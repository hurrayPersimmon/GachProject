package com.f2z.gach.DataGetter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class dataService {
    private final dataRepository dataRepository;
    private final nodeRepository nodeRepository;

    public dataEntity getData(dataDto datadto) {
        log.info(datadto.toString());
        dataEntity data = datadto.toEntity();
        log.info(data.getNode2());
        log.info(data.getNode1());
//        nodeEntity nodeOne = nodeRepository.findByNodeId(Integer.valueOf(data.getNode1()));
        nodeEntity nodeOne = nodeRepository.findByNodeName(data.getNode1());
        log.info(" nodeOne "+nodeOne.getNodeName());
        nodeEntity nodeTwo = nodeRepository.findByNodeName(data.getNode2());
        log.info(nodeTwo.getNodeName());


        double distance = getDistance(nodeTwo, nodeOne);
        double deltaAltitude = nodeTwo.getNodeAltitude() - nodeOne.getNodeAltitude();
        double angle = Math.toDegrees(Math.atan(deltaAltitude/ distance));
        data.setWeightShortest(distance);
        data.setWeightOptimal(angle);
        return dataRepository.save(data);
    }

    //하버 사인 거리 구하기.
    private static double getDistance(nodeEntity nodeTwo, nodeEntity nodeOne) {
        final double R = 6371.01;
        double dLat = Math.toRadians(nodeTwo.getNodeLatitude() - nodeOne.getNodeLatitude());
        double dLon = Math.toRadians(nodeTwo.getNodeLongitude() - nodeOne.getNodeLongitude());
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(Math.toRadians(nodeOne.getNodeLatitude())) * Math.cos(Math.toRadians(nodeTwo.getNodeLatitude())) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }

    public void getDatabases() {
        List<dataEntity> list = dataRepository.findAll();
        exportToCSV(list, "/home/t24102/data.csv");
    }

    public void exportToCSV(List<dataEntity> dataList, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // CSV 파일 헤더 작성
            writer.write("dataId,createDt,birthYear,gender, height, node1, node2, " +
                    "precipitation, precipitationProbability, takeTime, " +
                    "temperature, walkSpeed, weight, weightOptimal, weightShortest\n");

            // 데이터 추출 및 CSV 파일에 쓰기
            for (dataEntity data : dataList) {
                // 데이터 엔터티에서 필요한 필드를 가져와 CSV 형식으로 작성
                String csvLine = String.format("%s,%s,%s,%s, %s, %s,%s,%s,%s, %s, %s,%s,%s,%s\n",
                        data.getDataId(), data.getCreateDt(), data.getBirthYear(),
                        data.getHeight(), data.getNode1(), data.getNode2(),
                        data.getPrecipitation(), data.getPrecipitationProbability(),
                        data.getTakeTime(), data.getTemperature(), data.getWalkSpeed(),
                        data.getWeight(), data.getWeightOptimal(), data.getWeightShortest());
                writer.write(csvLine);
            }

            log.info("CSV 파일이 성공적으로 생성되었습니다.");
        } catch (IOException e) {
            log.info("CSV 파일을 생성하는 동안 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
