package com.f2z.gach.EnumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Authorization {
    ADMIN("ROLE_ADMIN"),
    GUEST("ROLE_GUEST"),
    WAITER("ROLE_WATIER");

    private String value;
}
