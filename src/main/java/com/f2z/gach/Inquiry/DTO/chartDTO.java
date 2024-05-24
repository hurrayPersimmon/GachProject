package com.f2z.gach.Inquiry.DTO;

import lombok.*;

import java.time.LocalDate;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class chartDTO {
    private LocalDate date;
    private Long count;


    public chartDTO(java.sql.Date date, Long count) {
        this.date = date.toLocalDate();
        this.count = count;
    }
}
