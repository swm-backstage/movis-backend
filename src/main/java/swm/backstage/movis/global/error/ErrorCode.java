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
    USER_NOT_FOUND("C005", "User Not Found", DisplayType.POPUP),
    INVALID_PASSWORD("C006", "Invalid Password", DisplayType.POPUP),
    INVALID_TOKEN_FORMAT("C007", "Invalid Token Format", DisplayType.POPUP),
    EXPIRED_TOKEN("C008", "Expired Token", DisplayType.POPUP),
    INVALID_TOKEN("C009", "Invalid Token", DisplayType.POPUP),
    DUPLICATE_USER("C010", "Duplicate User", DisplayType.POPUP),
    UNAUTHORIZED_PERMISSION("C011", "Unauthorized Permission", DisplayType.POPUP),
    UNAUTHENTICATED_REQUEST("C012", "Unauthenticated Request", DisplayType.POPUP),
    DECRYPTION_FAILED("C013", "Decryption Failed", DisplayType.POPUP);

    private final String code;
    private final String message;
    private final DisplayType type;

    ErrorCode(final String code, final String message, DisplayType type) {
        this.message = message;
        this.code = code;
        this.type = type;
    }


}