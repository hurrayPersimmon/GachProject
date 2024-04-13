package com.f2z.gach.Response;

import lombok.Data;

@Data

public class ResponseEntity<T> {
    private boolean success;
    private int property;
    private String message;
    private T data;
}
