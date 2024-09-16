package swm.backstage.movis.domain.invitation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.invitation.VerifyCode;
import swm.backstage.movis.domain.invitation.repository.VerifyCodeRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerifyService {
    private final VerifyCodeRepository verifyCodeRepository;

    // TODO : redis로 변경
    public void saveVerifyCode(String phoneNumber, String verifyCode) {
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(3);
        verifyCodeRepository.findByPhoneNumber(phoneNumber).ifPresent(verifyCodeRepository::delete);
        verifyCodeRepository.save(new VerifyCode(phoneNumber, verifyCode, expiredAt));
    }

    // TODO : redis의 TTL로 처리
    public void deleteVerifyCode(String phoneNumber) {
        verifyCodeRepository.findByPhoneNumber(phoneNumber).ifPresent(verifyCodeRepository::delete);
    }

    // TODO : redis로 변경
    public boolean verifyPhoneNumber(String phoneNumber, String verifyCode) {
        return verifyCodeRepository.findByPhoneNumber(phoneNumber)
                .filter(verify -> verify.getVerifyCode().equals(verifyCode))
                .filter(verify -> verify.getExpiredAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public boolean isVerifiedPhoneNumber(String phoneNumber) {
        return verifyCodeRepository.findByPhoneNumber(phoneNumber)
                .filter(VerifyCode::isVerified)
                .filter(verify -> verify.getVerifiedExpiredAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}
