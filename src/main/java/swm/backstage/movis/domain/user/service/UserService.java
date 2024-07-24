package swm.backstage.movis.domain.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.user.User;
import swm.backstage.movis.domain.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    Optional<User> findByIdentifier(String identifier) {

        return userRepository.findByIdentifier(identifier);
    }
}
