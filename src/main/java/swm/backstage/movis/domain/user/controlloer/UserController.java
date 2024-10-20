package swm.backstage.movis.domain.user.controlloer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.backstage.movis.domain.auth.dto.AuthenticationPrincipalDetails;
import swm.backstage.movis.domain.user.dto.response.UserGetResDto;
import swm.backstage.movis.domain.user.service.UserService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserGetResDto getUserByToken(@AuthenticationPrincipal AuthenticationPrincipalDetails principal){

        return new UserGetResDto(userService.findByIdentifier(principal.getIdentifier())
                .orElseThrow(()-> new BaseException("Element Not Found", ErrorCode.ELEMENT_NOT_FOUND)));
    }
}
