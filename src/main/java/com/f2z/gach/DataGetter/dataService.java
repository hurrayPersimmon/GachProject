package com.f2z.gach.DataGetter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
