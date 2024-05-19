package com.f2z.gach.DataGetter;

import com.f2z.gach.EnumType.Gender;
import com.f2z.gach.EnumType.Speed;
import com.f2z.gach.History.Entity.HistoryLineTime;
import com.f2z.gach.History.Entity.UserHistory;
import com.f2z.gach.User.Entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class dataEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataId;
    private String node1;
    private String node2;
    private Integer birthYear;
    private Integer gender;
    private Double height;
    private Double weight;
    private Integer walkSpeed;
    private Double temperature;
    private Integer precipitationProbability;
    private Double precipitation;
    private Integer takeTime;
    private Double weightShortest;
    private Double weightOptimal;

    public static dataEntity parseHistory(HistoryLineTime history){
        User tempuser = history.getUserHistory().getUser();
        UserHistory userHistory = history.getUserHistory();
        dataEntity data = new dataEntity();
        data.setBirthYear(tempuser.getUserBirth());
        data.setHeight(tempuser.getUserHeight());
        data.setWeight(tempuser.getUserWeight());
        data.setPrecipitation(userHistory.getRainPrecipitation());
        data.setPrecipitationProbability(userHistory.getRainPrecipitationProbability());
        data.setTemperature(userHistory.getTemperature());
        data.setTakeTime(history.getLineTime());
        if(tempuser.getUserGender() == Gender.남){
            data.setGender(0);
        } else {
            data.setGender(1);
        }
        if(tempuser.getUserSpeed() == Speed.FAST){
            data.setWalkSpeed(2);
        } else if(tempuser.getUserSpeed() == Speed.SLOW){
            data.setWalkSpeed(0);
        } else {
            data.setWalkSpeed(1);
        }

        data.setWeightOptimal(history.getMapLine().getWeightOptimal());
        data.setWeightShortest(history.getMapLine().getWeightShortest());
        return data;
    }
}