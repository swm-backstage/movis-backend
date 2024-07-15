package swm.backstage.movis.global.error.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.global.error.ErrorCode;


@Getter
@NoArgsConstructor
public class BaseException extends RuntimeException{
    private ErrorCode errorCode;

    public BaseException(String message,final ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
