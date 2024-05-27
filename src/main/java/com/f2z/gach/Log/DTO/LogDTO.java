package com.f2z.gach.Log.DTO;


import com.f2z.gach.EnumType.LogLevel;
import com.f2z.gach.EnumType.Properties;
import com.f2z.gach.Log.Entity.Log;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public class LogDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class LogListStructure {
        private Integer logId;
        private LocalDateTime createDt;
        private LogLevel logLevel;
        private Properties property;
        private String message;
        private String url;
        private String httpMethod;

        public static LogListStructure toLogListStructure(Log log){
            return LogListStructure.builder()
                    .logId(log.getLogId())
                    .createDt(log.getCreateDt())
                    .logLevel(log.getLogLevel())
                    .property(log.getProperty())
                    .message(log.getMessage())
                    .url(log.getUrl())
                    .httpMethod(log.getHttpMethod())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class AdminLogList {
        List<LogListStructure> adminLogList;
        Integer totalPage;
        Long totalElements;
    }

    public static AdminLogList toAdminlogList(Page<Log> logPage, List<LogListStructure> loglist){
        return AdminLogList.builder()
                .adminLogList(loglist)
                .totalPage(logPage.getTotalPages())
                .totalElements(logPage.getTotalElements())
                .build();
    }
}
