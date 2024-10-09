package swm.backstage.movis.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.user.User;
import swm.backstage.movis.domain.user.repository.UserRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByIdentifier(String identifier) {

        return userRepository.findByIdentifier(identifier);
    }

    public User findUserWithInfoByIdentifier(String identifier) {
        return userRepository.findUserWithClubUserAndClubAndAccountBook(identifier)
                .orElseThrow(()-> new BaseException("유저를 찾을 수 없습니다.", ErrorCode.ELEMENT_NOT_FOUND));
    }
}
