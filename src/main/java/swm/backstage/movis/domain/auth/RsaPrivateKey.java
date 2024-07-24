package swm.backstage.movis.domain.auth;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RsaPrivateKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2048)
    private String privateKeyString;

    public RsaPrivateKey(String privateKeyString) {
        this.privateKeyString = privateKeyString;
    }
}
