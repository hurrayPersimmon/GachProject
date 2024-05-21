package com.f2z.gach.Log.Entity;

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
@Builder
public class Log extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logId;
    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;
    private Properties property;
    private String message;
    private String url;
    private String httpMethod;
}
