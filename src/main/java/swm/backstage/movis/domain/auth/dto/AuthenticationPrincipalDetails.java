package swm.backstage.movis.domain.auth.dto;

import lombok.Getter;

@Getter
public class AuthenticationPrincipalDetails {

    private final String identifier;
    private final String platformType;

    public AuthenticationPrincipalDetails(String identifier, String platformType) {
        this.identifier = identifier;
        this.platformType = platformType;
    }
}
