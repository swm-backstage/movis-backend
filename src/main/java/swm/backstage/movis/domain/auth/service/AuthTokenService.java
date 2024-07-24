package swm.backstage.movis.domain.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.auth.AuthToken;
import swm.backstage.movis.domain.auth.repository.AuthTokenRepository;

import java.util.Optional;


/**
 * TODO: MySQL -> Redis
 * */
@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final AuthTokenRepository authTokenRepository;

    @Transactional
    public void addAuthToken(String identifier, String accessToken, String refreshToken){

        authTokenRepository.save(new AuthToken(
                identifier,
                accessToken,
                refreshToken
        ));
    }

    @Transactional
    public void deleteAllByIdentifier(String identifier){

        authTokenRepository.deleteAllByIdentifier(identifier);
    }

    public Optional<AuthToken> findByIdentifier(String identifier){

        return authTokenRepository.findByIdentifier(identifier);
    }
}
