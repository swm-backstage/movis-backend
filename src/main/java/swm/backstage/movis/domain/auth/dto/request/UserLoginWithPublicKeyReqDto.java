package swm.backstage.movis.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UserLoginWithPublicKeyReqDto {

    @NotNull
    private Long rsaPrivateKeyId;

    @NotNull
    private String encryptedIdentifier;

    @NotNull
    private String encryptedPassword;
}
