package com.f2z.gach.Entity;

import com.f2z.gach.Entity.EnumType.LogLevel;
import com.f2z.gach.Entity.EnumType.Properties;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.springframework.http.HttpMethod;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
