package com.f2z.gach.DataGetter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {
    private boolean success;
    private int property;
    private String message;
    private T data;
}
