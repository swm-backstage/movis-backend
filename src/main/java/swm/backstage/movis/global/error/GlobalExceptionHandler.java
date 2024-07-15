package swm.backstage.movis.global.error;


import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import swm.backstage.movis.global.error.exception.BaseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 지원하지 않는 HTTP Method로 요청시 발생
     * */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
    }

    /**
     * HttpMessageConverter가 binding 하지 못할 경우 발생
     * */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE);
    }

    /**
     * 기타 에러 예외 처리시 발생 -> 400에러로 메시지 커스텀 하고 싶을 때 사용
     * */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BaseException.class)
    public ErrorResponse handleElementNotFoundException(BaseException e) {
        return ErrorResponse.of(e.getMessage(),e.getErrorCode());
    }

}
