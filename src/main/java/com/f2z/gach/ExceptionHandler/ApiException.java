package com.f2z.gach.ExceptionHandler;

import com.f2z.gach.Entity.EnumType.Properties;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{
    private Properties status;

    public ApiException(Properties status) {
        super(status.getMessage());
        this.status = status;
    }

    public ApiException(Properties status, String message) {
        super(message);
        this.status = status;
    }

    public ApiException(boolean success, Integer property, String message, Exception data) {
    }
}
