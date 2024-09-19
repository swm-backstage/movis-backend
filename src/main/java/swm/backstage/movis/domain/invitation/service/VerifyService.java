package swm.backstage.movis.domain.invitation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.invitation.VerifyCode;
import swm.backstage.movis.domain.invitation.repository.VerifyCodeRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

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
        System.out.println(verifyCodeRepository.findByPhoneNumber(phoneNumber).toString());
        verifyCodeRepository.findByPhoneNumber(phoneNumber).ifPresent(verifyCodeRepository::delete);
    }

    // TODO : redis로 변경
    public boolean verifyPhoneNumber(String phoneNumber, String verifyCode) {
        VerifyCode selectedVerifyCode = verifyCodeRepository.findByPhoneNumber(phoneNumber)
                .filter(verify -> verify.getVerifyCode().equals(verifyCode))
                .filter(verify -> verify.getExpiredAt().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new BaseException("인증번호가 일치하지 않습니다.", ErrorCode.ELEMENT_NOT_FOUND));
        selectedVerifyCode.setVerified(true);
        selectedVerifyCode.setVerifiedExpiredAt(LocalDateTime.now().plusHours(1));
        verifyCodeRepository.save(selectedVerifyCode);
        return selectedVerifyCode.isVerified();
    }

    public boolean isVerifiedPhoneNumber(String phoneNumber) {
        return verifyCodeRepository.findByPhoneNumber(phoneNumber)
                .filter(VerifyCode::isVerified)
                .filter(verify -> verify.getVerifiedExpiredAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}
