package com.f2z.gach.Event.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventLocationDTO {
    private Integer eventInfoId;
    private String eventPlaceName;
    private Double eventAltitude;
    private Double eventLatitude;
    private Double eventLongitude;
}
