package swm.backstage.movis.domain.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;
    private String accessToken;
    private String refreshToken;

    public AuthToken(String identifier, String accessToken, String refreshToken) {
        this.identifier = identifier;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}