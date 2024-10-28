package swm.backstage.movis.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserIdentifierGetReqDto {

    @NotNull
    private String phoneNo;
}
