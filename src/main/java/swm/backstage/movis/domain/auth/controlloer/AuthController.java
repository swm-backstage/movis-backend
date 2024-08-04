package swm.backstage.movis.domain.auth.controlloer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.auth.dto.request.*;
import swm.backstage.movis.domain.auth.dto.response.ConfirmIdentifierResDto;
import swm.backstage.movis.domain.auth.dto.response.JwtCreateResDto;
import swm.backstage.movis.domain.auth.dto.response.PublicKeyGetResDto;
import swm.backstage.movis.domain.auth.dto.response.UserLoginResDto;
import swm.backstage.movis.domain.auth.service.AuthService;
import swm.backstage.movis.domain.auth.utils.JwtUtil;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/test/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserCreateReqDto userCreateReqDto) {

        return authService.register(userCreateReqDto);
    }

    @PostMapping("/test/login")
    public UserLoginResDto login(@RequestBody @Validated UserLoginReqDto userLoginReqDto) {

        return authService.login(userLoginReqDto);
    }

    @PostMapping("/test/confirmIdentifier")
    public ConfirmIdentifierResDto confirmIdentifier(@RequestBody ConfirmIdentifierReqDto confirmIdentifierReqDto) {

        return authService.confirmIdentifier(confirmIdentifierReqDto);
    }

    /**
     * register with publicKey
     * */
    @PostMapping("/register")
    public ResponseEntity<?> registerWithPublicKey(@RequestBody @Validated UserCreateWithPublicKeyReqDto userCreateWithPublicKeyReqDto) {

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

    @GetMapping("/test/manager")
    public ResponseEntity<?> getManagerTest(){

        return ResponseEntity.ok(200);
    }
}

