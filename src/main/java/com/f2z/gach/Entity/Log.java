package com.f2z.gach.Entity;

import com.f2z.gach.Config.BaseTimeEntity;
import com.f2z.gach.EnumType.LogLevel;
import com.f2z.gach.EnumType.Properties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpMethod;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Log extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;
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
