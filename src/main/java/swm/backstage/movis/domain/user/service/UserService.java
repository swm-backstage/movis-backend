package swm.backstage.movis.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.invitation.service.VerifyService;
import swm.backstage.movis.domain.user.User;
import swm.backstage.movis.domain.user.repository.UserRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VerifyService verifyService;

    // TODO: 해당 계층에서 커스텀 예외 처리
    public Optional<User> findByIdentifier(String identifier) {

        return userRepository.findByIdentifier(identifier);
    }

    public User findByPhoneNo(String phoneNo) {

        if (!verifyService.isVerifiedPhoneNumber(phoneNo)) {
            throw new BaseException("인증되지 않은 번호입니다 : " + phoneNo, ErrorCode.UNAUTHENTICATED_REQUEST);
        }

        return userRepository.findByPhoneNo(phoneNo)
                .orElseThrow(()-> new BaseException("해당 번호로 가입된 유저를 찾을 수 없습니다.", ErrorCode.ELEMENT_NOT_FOUND));
    }

    public User findUserWithInfoByIdentifier(String identifier) {
        return userRepository.findUserWithClubUserAndClubAndAccountBook(identifier)
                .orElseThrow(()-> new BaseException("유저를 찾을 수 없습니다.", ErrorCode.ELEMENT_NOT_FOUND));
    }
}
