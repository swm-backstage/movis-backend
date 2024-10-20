package swm.backstage.movis.domain.auth.controlloer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.auth.dto.request.*;
import swm.backstage.movis.domain.auth.dto.response.*;
import swm.backstage.movis.domain.auth.service.AuthService;
import swm.backstage.movis.domain.auth.utils.JwtUtil;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/test/register")
    public UserCreateResDto register(@RequestBody @Validated UserCreateReqDto userCreateReqDto) {

        return authService.register(userCreateReqDto);
    }

    @PostMapping("/test/login")
    public UserLoginResDto login(@RequestBody @Validated UserLoginReqDto userLoginReqDto) {

        return authService.login(userLoginReqDto);
    }

    @GetMapping("/{identifier}/exists")
    public CheckIdentifierResDto checkIdentifierExists(@PathVariable("identifier") String identifier) {

        return authService.confirmIdentifier(identifier);
    }

    /**
     * register with publicKey
     * */
    @PostMapping("/register")
    public UserCreateResDto registerWithPublicKey(@RequestBody @Validated UserCreateWithPublicKeyReqDto userCreateWithPublicKeyReqDto) {

        return authService.registerWithRsaPublicKey(userCreateWithPublicKeyReqDto);
    }

    /**
     * login with publicKey
     * */
    @PostMapping("/login")
    public UserLoginResDto loginWithPublicKey(@RequestBody @Validated UserLoginWithPublicKeyReqDto userLoginWithPublicKeyReqDto) {

        return authService.loginWithRsaPublicKey(userLoginWithPublicKeyReqDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        String BearerAccessToken = request.getHeader(jwtUtil.getACCESS_TOKEN_NAME());
        return authService.logout(BearerAccessToken);
    }

    @PatchMapping("/reissue")
    public JwtCreateResDto reissue(@RequestBody @Validated JwtCreateReqDto jwtCreateReqDto) {

        return authService.reissue(jwtCreateReqDto);
    }

    @GetMapping("/publicKey")
    public PublicKeyGetResDto getPublicKey() {

        return authService.getPublicKey();
    }
}

