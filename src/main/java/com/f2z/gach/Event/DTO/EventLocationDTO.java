package com.f2z.gach.Event.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventLocationDTO {
    private Integer eventInfoId;
    private Integer eventCode;
    private String eventName;
    private String eventPlaceName;
    private Double eventLatitude;
    private Double eventLongitude;
    private Double eventAltitude;


}
