package com.f2z.gach.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AiModelDTO {
    private Integer aiModelId;
    private Timestamp aiCreatedAt;
    private Double aiModelVersion;
    private Integer aiModelAccuracy;
    private String aiModelName;
    private String aiModelPath;


}
