package swm.backstage.movis.domain.auth.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UserLoginReqDto {

    @NotNull
    @Schema(example = "rlfehd12")
    private String identifier;

    @NotNull
    @Schema(example = "Ghdrlfehd112@")
    private String password;

    public UserLoginReqDto(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }
}
