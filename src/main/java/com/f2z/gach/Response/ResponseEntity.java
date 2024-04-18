package com.f2z.gach.Response;

import com.f2z.gach.EnumType.Properties;
import lombok.*;

@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseEntity<T> {
    private final boolean success;
    private final Integer property;
    private final String message;
    private T data;

    public static <T> ResponseEntity<T> requestSuccess(T data){
        return new ResponseEntity<>(true, Properties.OK.getCode(), Properties.OK.getMessage(), data);
    }

    public static <T> ResponseEntity<T> saveSuccess(T data){
        return new ResponseEntity<>(true, Properties.CREATED.getCode(), Properties.CREATED.getMessage(), data);
    }

    public static <T> ResponseEntity<T> NotDuplicateID(String username){
        return new ResponseEntity<>(true, Properties.NOT_DUPLICATE_ID.getCode(), Properties.NOT_DUPLICATE_ID.getMessage(), (T) username);
    }

    public static <T> ResponseEntity<T> saveButNoContent(T data) {
        return new ResponseEntity<>(true, Properties.NO_CONTENT.getCode(), Properties.NO_CONTENT.getMessage(), data);
    }

    public static <T> ResponseEntity<T> loginSuccess(T data){
        return new ResponseEntity<>(true, Properties.LOGIN_SUCCESS.getCode(), Properties.LOGIN_SUCCESS.getMessage(), data);
    }

    public static <T> ResponseEntity<T> DuplicateID(String username){
        return new ResponseEntity<>(false, Properties.DUPLICATE_ID.getCode(), Properties.DUPLICATE_ID.getMessage(), (T) username);
    }

    public static <T> ResponseEntity<T> wrongUsername(T data){
        return new ResponseEntity<>(false, Properties.WRONG_USERNAME.getCode(), Properties.WRONG_USERNAME.getMessage(), data);
    }

    public static <T> ResponseEntity<T> wrongPassword(T data){
        return new ResponseEntity<>(false, Properties.WRONG_PASSWORD.getCode(), Properties.WRONG_PASSWORD.getMessage(), data);
    }

    public static <T> ResponseEntity<T> notFound(T data){
        return new ResponseEntity<>(false, Properties.NOT_FOUND.getCode(), Properties.NOT_FOUND.getMessage(), data);
    }

    public static <T> ResponseEntity<T> fail(T data){
        return new ResponseEntity<>(false, Properties.BAD_REQUEST.getCode(), Properties.BAD_REQUEST.getMessage(), data);
    }
}
