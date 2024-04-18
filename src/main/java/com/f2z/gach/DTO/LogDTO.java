package com.f2z.gach.DTO;

import com.f2z.gach.EnumType.LogLevel;
import com.f2z.gach.EnumType.Properties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpMethod;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogDTO {
    private Integer logId;
    private Timestamp logCreatedAt;
    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;
    private Properties property;
    private Long userCode;
    private Integer guestCode;
    private Integer adminCode;
    private String message;
    private String url;
    private HttpMethod httpMethod;
}
