package swm.backstage.movis.domain.club_user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClubUserCreateReqDto {

    @NotNull
    private String clubId;

    @NotNull
    private String identifier;
}
