package com.f2z.gach.Inquiry.DTO;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class chartDTO {
    private LocalDate date;
    private Long count;
}
