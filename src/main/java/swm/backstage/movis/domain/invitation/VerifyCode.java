package swm.backstage.movis.domain.invitation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

// TODO : redis로 변경
@Entity
@Getter
@NoArgsConstructor
public class VerifyCode {
    @Id
    private Long id;
    private String phoneNumber;
    private String verifyCode;
    private LocalDateTime expiredAt;
    private boolean verified;
    @Setter
    private LocalDateTime verifiedExpiredAt;

    public VerifyCode(String phoneNumber, String verifyCode, LocalDateTime expiredAt) {
        this.phoneNumber = phoneNumber;
        this.verifyCode = verifyCode;
        this.expiredAt = expiredAt;
        this.verified = false;
    }

}