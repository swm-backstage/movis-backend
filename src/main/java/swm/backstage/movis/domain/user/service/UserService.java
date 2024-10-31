package swm.backstage.movis.domain.user.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.auth.utils.SHA256PasswordEncoder;
import swm.backstage.movis.domain.invitation.service.VerifyService;
import swm.backstage.movis.domain.user.User;
import swm.backstage.movis.domain.user.repository.UserRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VerifyService verifyService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SHA256PasswordEncoder sha256PasswordEncoder;

    // TODO: 해당 계층에서 커스텀 예외 처리
    public User findByIdentifier(String identifier) {

        return userRepository.findByIdentifierAndIsDeleted(identifier, Boolean.FALSE)
                .orElseThrow(() -> new BaseException("유저를 찾을 수 없습니다. ", ErrorCode.ELEMENT_NOT_FOUND));
    }

    public User findByPhoneNo(String phoneNo) {

        if (!verifyService.isVerifiedPhoneNumber(phoneNo)) {
            throw new BaseException("인증되지 않은 번호입니다 : " + phoneNo, ErrorCode.UNAUTHENTICATED_REQUEST);
        }

        return userRepository.findByPhoneNoAndIsDeleted(phoneNo, Boolean.FALSE)
                .orElseThrow(()-> new BaseException("해당 번호로 가입된 유저를 찾을 수 없습니다.", ErrorCode.ELEMENT_NOT_FOUND));
    }

    @Transactional
    public void updatePassword(String identifier, String oldPassword, String newPassword) {

        if (oldPassword.equals(newPassword)){

            throw new BaseException("이전 비밀번호와 동일합니다.", ErrorCode.INVALID_PASSWORD);
        }

        User user = this.findByIdentifier(identifier);

        String encryptedOldPasswordWithSHA256 = sha256PasswordEncoder.encodeWithSalt(oldPassword, user.getUuid());
        if (!bCryptPasswordEncoder.matches(encryptedOldPasswordWithSHA256, user.getPassword())){

            throw new BaseException("비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }

        String encryptedNewPasswordWithSHA256 = sha256PasswordEncoder.encodeWithSalt(newPassword, user.getUuid());
        String encryptedNewPasswordWithSHA256AndBCrypt = bCryptPasswordEncoder.encode(encryptedNewPasswordWithSHA256);

        user.updatePassword(encryptedNewPasswordWithSHA256AndBCrypt);
    }
}
