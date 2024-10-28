package swm.backstage.movis.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserIdentifierGetResDto {

    private String identifier;

    public UserIdentifierGetResDto(String identifier) {
        this.identifier = identifier;
    }
}
