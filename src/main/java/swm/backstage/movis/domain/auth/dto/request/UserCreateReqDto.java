package swm.backstage.movis.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UserCreateReqDto {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$",
            message = "아이디는 4자리에서 20자리 사이의 영문자와 숫자만 가능합니다.")
    @Schema(example = "rlfehd12")
    private String identifier;

    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
            message = "8자리에서 20자리, 영 대/소문자, 숫자 한 번씩 포함, @, #, $, %, ^, &, +, =등의 문자를 한 번씩 포함, 띄어쓰기/탭 금지")
    @Schema(example = "Ghdrlfehd112@")
    private String password;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z가-힣]{3,10}$",
            message = "이름은 3자리에서 10자리 사이의 한글 또는 영문자만 가능합니다.")
    @Schema(example = "홍길동")
    private String name;

    @NotNull
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
    @Schema(example = "010-1234-5678")
    private String phoneNo;

    public UserCreateReqDto(String identifier, String password, String name, String phoneNo) {
        this.identifier = identifier;
        this.password = password;
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public void convertPasswordToEncrypted(String encryptedPassword) {

        this.password = encryptedPassword;
    }
}