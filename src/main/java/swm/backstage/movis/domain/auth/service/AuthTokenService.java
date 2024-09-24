package swm.backstage.movis.domain.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.auth.AuthToken;
import swm.backstage.movis.domain.auth.PlatformType;
import swm.backstage.movis.domain.auth.dto.AuthTokenDto;
import swm.backstage.movis.domain.auth.repository.AuthTokenRepository;
import swm.backstage.movis.domain.auth.utils.JwtUtil;

import java.util.Optional;


/**
 * TODO: MySQL -> Redis
 * */
@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final AuthTokenRepository authTokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthTokenDto issueAuthToken(String identifier, String platformType) {

        String accessToken = jwtUtil.createToken(
                jwtUtil.getACCESS_TOKEN_NAME(),
                platformType,
                identifier,
                jwtUtil.getACCESS_TOKEN_EXPIRED_TIME()
        );
        String refreshToken = jwtUtil.createToken(
                jwtUtil.getREFRESH_TOKEN_NAME(),
                platformType,
                identifier,
                jwtUtil.getREFRESH_TOKEN_EXPIRED_TIME()
        );

        this.deleteAllByIdentifier(identifier);
        return new AuthTokenDto(this.addAuthToken(identifier, accessToken, refreshToken));
    }

    @Transactional
    public AuthToken addAuthToken(String identifier, String accessToken, String refreshToken){

        return authTokenRepository.save(new AuthToken(
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
