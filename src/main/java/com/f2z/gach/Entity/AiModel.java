package com.f2z.gach.Entity;

import com.f2z.gach.Entity.EnumType.Satisfaction;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class AiModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aiModelId;
    private Timestamp aiCreatedAt;
    private Double aiModelVersion;
    private Integer aiModelAccuracy;
    private String aiModelName;
    private String aiModelPath;


}
