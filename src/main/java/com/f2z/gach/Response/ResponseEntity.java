package com.f2z.gach.Response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseEntity<T> {
    private boolean success;
    private HttpStatus property;
    private String message;
    private T data;

}
