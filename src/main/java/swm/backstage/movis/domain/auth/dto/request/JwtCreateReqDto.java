package swm.backstage.movis.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JwtCreateReqDto {

    @NotNull
    @JsonProperty("refresh-token")
    String refreshToken;
}
