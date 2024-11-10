package swm.backstage.movis.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.user.User;
import swm.backstage.movis.domain.user.repository.UserRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

@Service
@RequiredArgsConstructor
public class UserManager {

    private final UserRepository userRepository;;

    public User findByIdentifier(String identifier) {

        return userRepository.findByIdentifierAndIsDeleted(identifier, Boolean.FALSE)
                .orElseThrow(() -> new BaseException("유저를 찾을 수 없습니다. ", ErrorCode.ELEMENT_NOT_FOUND));
    }
    public User findByPhoneNo(String phoneNo) {

        return userRepository.findByPhoneNoAndIsDeleted(phoneNo, Boolean.FALSE)
                .orElseThrow(() -> new BaseException("유저를 찾을 수 없습니다. ", ErrorCode.ELEMENT_NOT_FOUND));
    }

    public User findUserWithInfoByIdentifier(String identifier) {

        return userRepository.findUserWithClubUserAndClubAndAccountBook(identifier)
                .orElseThrow(()-> new BaseException("유저를 찾을 수 없습니다.", ErrorCode.ELEMENT_NOT_FOUND));
    }
}
