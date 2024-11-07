package swm.backstage.movis.domain.auth.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import swm.backstage.movis.domain.auth.AuthToken;
import swm.backstage.movis.domain.auth.dto.response.*;
import swm.backstage.movis.domain.auth.enums.PlatformType;
import swm.backstage.movis.domain.auth.RsaPrivateKey;
import swm.backstage.movis.domain.auth.dto.AuthTokenDto;
import swm.backstage.movis.domain.auth.dto.RSAKeyPairDto;
import swm.backstage.movis.domain.auth.dto.request.*;
import swm.backstage.movis.domain.auth.repository.RsaPrivateKeyRepository;
import swm.backstage.movis.domain.auth.utils.JwtUtil;
import swm.backstage.movis.domain.auth.utils.RsaUtil;
import swm.backstage.movis.domain.auth.utils.SHA256PasswordEncoder;
import swm.backstage.movis.domain.invitation.service.VerifyService;
import swm.backstage.movis.domain.user.User;
import swm.backstage.movis.domain.user.repository.UserRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SHA256PasswordEncoder sha256PasswordEncoder;
    private final JwtUtil jwtUtil;
    private final RsaUtil rsaUtil;
    private final AuthTokenService authTokenService;
    private final RsaPrivateKeyRepository rsaPrivateKeyRepository;
    private final VerifyService verifyService;


    @Transactional
    public UserCreateResDto register(@Validated UserCreateReqDto userCreateReqDto) {

        if (!verifyService.isVerifiedPhoneNumber(userCreateReqDto.getPhoneNo().replaceAll("-", ""))) {

            throw new BaseException("인증되지 않은 번호입니다 : " + userCreateReqDto.getPhoneNo(), ErrorCode.UNAUTHENTICATED_REQUEST);
        }

        this.validateExistingUser(userRepository.findByIdentifier(userCreateReqDto.getIdentifier()));
        this.validateExistingUser(userRepository.findByPhoneNo(userCreateReqDto.getPhoneNo()));

        String userUid = UUID.randomUUID().toString();

        String plainPassword = userCreateReqDto.getPassword();
        String encryptedPasswordWithSHA256 = sha256PasswordEncoder.encodeWithSalt(plainPassword, userUid);
        String encryptedPasswordWithSHA256AndBCrypt = bCryptPasswordEncoder.encode(encryptedPasswordWithSHA256);

        userCreateReqDto.convertPasswordToEncrypted(encryptedPasswordWithSHA256AndBCrypt);

        User registeredUser = userRepository.save(new User(userUid, userCreateReqDto));

        return new UserCreateResDto(registeredUser);
    }

    /**
     * POST /api/auth/v2/register
     * */
    @Transactional
    public UserCreateResDto registerWithRsaPublicKey(UserCreateWithPublicKeyReqDto userCreateWithPublicKeyReqDto) {

        Long rsaPrivateKeyId = userCreateWithPublicKeyReqDto.getRsaPrivateKeyId();

        String rsaPrivateKeyString = rsaPrivateKeyRepository.findById(rsaPrivateKeyId).orElseThrow(() ->
                new BaseException("", ErrorCode.DECRYPTION_FAILED))
                .getPrivateKeyString();
        rsaPrivateKeyRepository.deleteById(rsaPrivateKeyId);

        UserCreateReqDto userCreateReqDto = new UserCreateReqDto(
                rsaUtil.decryptData(userCreateWithPublicKeyReqDto.getEncryptedIdentifier(), rsaPrivateKeyString),
                rsaUtil.decryptData(userCreateWithPublicKeyReqDto.getEncryptedPassword(), rsaPrivateKeyString),
                rsaUtil.decryptData(userCreateWithPublicKeyReqDto.getEncryptedName(), rsaPrivateKeyString),
                rsaUtil.decryptData(userCreateWithPublicKeyReqDto.getEncryptedPhoneNo(), rsaPrivateKeyString)
        );

        return this.register(userCreateReqDto);
    }

    /**
     * 회원 탈퇴된 사용자의 identifier도 중복 검사에 포함된다.
     * */
    public CheckIdentifierResDto confirmIdentifier(String identifier) {

        return new CheckIdentifierResDto(userRepository.existsByIdentifier(identifier));
    }

    /**
     * POST /api/auth/v1/login
     * */
    @Transactional
    public UserLoginResDto login(UserLoginReqDto userLoginReqDto){

        String identifier = userLoginReqDto.getIdentifier();
        String password = userLoginReqDto.getPassword();

        User user = userRepository.findByIdentifierAndIsDeleted(identifier, Boolean.FALSE)
                .orElseThrow(() -> new BaseException("해당 사용자를 찾을 수 없습니다.", ErrorCode.USER_NOT_FOUND));

        String encryptedPasswordWithSHA256 = sha256PasswordEncoder.encodeWithSalt(password, user.getUuid());

        if (!bCryptPasswordEncoder.matches(encryptedPasswordWithSHA256, user.getPassword())){

            throw new BaseException("비밀번호가 일치하지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }

        AuthTokenDto authTokenDto = authTokenService.issueAuthToken(identifier, PlatformType.APP.value());

        return new UserLoginResDto(
                authTokenDto.getAccessToken(),
                authTokenDto.getRefreshToken()
        );
    }

    /**
     * POST /api/auth/v2/login
     * */
    @Transactional
    public UserLoginResDto loginWithRsaPublicKey(UserLoginWithPublicKeyReqDto userLoginWithPublicKeyReqDto){

        Long rsaPrivateKeyId = userLoginWithPublicKeyReqDto.getRsaPrivateKeyId();

        String rsaPublicKeyString = rsaPrivateKeyRepository.findById(rsaPrivateKeyId).orElseThrow(() ->
                        new BaseException("", ErrorCode.DECRYPTION_FAILED))
                .getPrivateKeyString();
        rsaPrivateKeyRepository.deleteById(rsaPrivateKeyId);

        UserLoginReqDto userLoginReqDto = new UserLoginReqDto(
                rsaUtil.decryptData(userLoginWithPublicKeyReqDto.getEncryptedIdentifier(), rsaPublicKeyString),
                rsaUtil.decryptData(userLoginWithPublicKeyReqDto.getEncryptedPassword(), rsaPublicKeyString)
        );

        return this.login(userLoginReqDto);
    }

    @Transactional
    public ResponseEntity<?> logout(String BearerAccessToken){

        String accessToken = jwtUtil.resolveToken(BearerAccessToken);
        String identifier = jwtUtil.getIdentifier(accessToken);

        authTokenService.deleteAllByIdentifier(identifier);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public JwtCreateResDto reissue(JwtCreateReqDto jwtCreateReqDto) {

        // refreshToken 유무 검사, Bearer 검사 x
        String refreshToken = jwtCreateReqDto.getRefreshToken();
        if (!StringUtils.hasText(refreshToken)) {

            throw new BaseException("옳바르지 않은 토큰 형식입니다. ", ErrorCode.INVALID_TOKEN_FORMAT);
        }

        // 토큰 유효성 검사
        try {

            jwtUtil.validateToken(refreshToken);
        } catch (ExpiredJwtException e) {

            throw new BaseException("리프레쉬 토큰이 만료되었습니다. 토큰을 재발급 해주세요. ", ErrorCode.EXPIRED_REFRESH_TOKEN);
        }
        catch (JwtException e) {

            throw new BaseException("유효하지 않은 토큰 입니다. 다시 로그인 해주세요. ", ErrorCode.INVALID_TOKEN);
        }

        // refreshToken 여부 검사
        if (!jwtUtil.getTokenType(refreshToken).equals(jwtUtil.getREFRESH_TOKEN_NAME())){

            throw new BaseException("유효하지 않은 토큰 입니다. 다시 로그인 해주세요. ", ErrorCode.INVALID_TOKEN);
        }

        // 요청된 refreshToken이 server에 있는 refreshToken과 일치하는지 검사.
        String identifier = jwtUtil.getIdentifier(refreshToken);
        AuthToken savedAuthToken = authTokenService.findByIdentifier(identifier).orElseThrow(() ->
                new BaseException("유효하지 않은 토큰 입니다. 다시 로그인 해주세요. ", ErrorCode.INVALID_TOKEN));
        if (!refreshToken.equals(savedAuthToken.getRefreshToken())) {

            throw new BaseException("유효하지 않은 토큰 입니다. 다시 로그인 해주세요. ", ErrorCode.INVALID_TOKEN);
        }

        AuthTokenDto authTokenDto = authTokenService.issueAuthToken(
                identifier,
                jwtUtil.getPlatformType(refreshToken)
        );

        return new JwtCreateResDto(
                authTokenDto.getAccessToken(),
                authTokenDto.getRefreshToken()
        );
    }

    @Transactional
    public PublicKeyGetResDto getPublicKey() {

        RSAKeyPairDto rsaKeyPairDto = rsaUtil.getRsaKeyPairDto();

        RsaPrivateKey savedRsaPrivateKey = rsaPrivateKeyRepository.save(new RsaPrivateKey(
                rsaKeyPairDto.getPublicKeyString()
        ));

        return new PublicKeyGetResDto(savedRsaPrivateKey.getId(), rsaKeyPairDto.getPublicKeyString()) ;
    }

    private void validateExistingUser(Optional<User> optionalUser) {

        optionalUser.ifPresent(user -> {
            if (!user.getIsDeleted()) {
                throw new BaseException("이미 존재하는 회원입니다.", ErrorCode.DUPLICATE_USER);
            } else {
                throw new BaseException("이미 탈퇴된 계정입니다. 탈퇴 복구 문의를 부탁드립니다. ", ErrorCode.DUPLICATE_CLUB_USER);
            }
        });
    }
}