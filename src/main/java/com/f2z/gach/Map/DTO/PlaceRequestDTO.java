package com.f2z.gach.Map.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PlaceRequestDTO {



    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class requestLocation{
        private Double latitude;
        private Double longitude;
        private Double altitude;
        private Integer placeId;
        private boolean isDepartures;
        public boolean isPlace(){
            if(placeId != null) return true;
            else return false;
        }
    }


}
