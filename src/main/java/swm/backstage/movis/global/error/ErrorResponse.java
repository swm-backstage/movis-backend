package swm.backstage.movis.global.error;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private DisplayType type;

    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.code = code.getCode();
        this.type = code.getType();
    }

    private ErrorResponse(final String message,final ErrorCode code) {
        this.message = message;
        this.code = code.getCode();
        this.type = code.getType();
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final String message,final ErrorCode code) {
        return new ErrorResponse(message, code);
    }

}
