package com.f2z.gach.Response;

import com.f2z.gach.EnumType.Properties;
import lombok.*;

@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseListEntity<T> {
    private final boolean success;
    private final Integer property;
    private final String message;
    private T[] data;

    public static <T> ResponseListEntity<T> requestListSuccess(T[] data){
        return new ResponseListEntity<>(true, Properties.OK.getCode(), Properties.OK.getMessage(), data);
    }
    public static <T> ResponseListEntity<T> sameNode(T[] data){
        return new ResponseListEntity<>(false, Properties.WRONG_REQUEST.getCode(), Properties.WRONG_REQUEST.getMessage(), data);
    }

    public static <T> ResponseListEntity<T> notFound(T[] data){
        return new ResponseListEntity<>(false, Properties.ID_NOT_FOUND.getCode(), Properties.ID_NOT_FOUND.getMessage(), data);
    }


}
