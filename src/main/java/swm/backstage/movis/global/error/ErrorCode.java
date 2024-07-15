package swm.backstage.movis.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

    // Common Error
    INTERNAL_SERVER_ERROR( "C001", "Server Error", DisplayType.POPUP),
    INVALID_INPUT_VALUE("C002", "Invalid Input Value", DisplayType.POPUP),
    METHOD_NOT_ALLOWED("C003", "Invalid HTTP Method", DisplayType.POPUP),
    ELEMENT_NOT_FOUND( "C004", "Element Not Found", DisplayType.POPUP),
    ;

    private final String code;
    private final String message;
    private final DisplayType type;

    ErrorCode(final String code, final String message, DisplayType type) {
        this.message = message;
        this.code = code;
        this.type = type;
    }


}