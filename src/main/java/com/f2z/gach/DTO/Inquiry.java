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
@Entity
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inquiryId;
    private boolean inquiryProgress;
    private Timestamp inquiryCreatedAt;
    private Long userCode;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryAnswer;



}
