package swm.backstage.movis.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDeleteReqDto {

    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
            message = "8자리에서 20자리, 영 대/소문자, 숫자 한 번씩 포함, @, #, $, %, ^, &, +, =등의 문자를 한 번씩 포함, 띄어쓰기/탭 금지")
    private String password;

}
