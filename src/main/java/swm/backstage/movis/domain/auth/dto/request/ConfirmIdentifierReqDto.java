package swm.backstage.movis.domain.auth.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ConfirmIdentifierReqDto {

    @NotNull
    private String identifier;
}
