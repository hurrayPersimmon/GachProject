package com.f2z.gach.User.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UserGuestResponseDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class respondGuestId {
        private Integer guestId;
    }


}
