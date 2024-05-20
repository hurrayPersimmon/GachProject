package com.f2z.gach.EnumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LogLevel {
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARN("WARN"),
    ERROR("ERROR"),
    FATAL("FATAL");

    private final String logLevel;

    public static LogLevel getLogLevel(Integer errorCode) {
        if(errorCode < 300) return INFO;
        else if(errorCode < 400) return WARN;
        else if(errorCode < 500) return ERROR;
        else return FATAL;
    }
}
