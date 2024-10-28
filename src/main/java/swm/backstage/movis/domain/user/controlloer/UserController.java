package swm.backstage.movis.domain.user.controlloer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.auth.dto.AuthenticationPrincipalDetails;
import swm.backstage.movis.domain.user.dto.UserGetResDto;
import swm.backstage.movis.domain.user.dto.UserIdentifierGetReqDto;
import swm.backstage.movis.domain.user.dto.UserIdentifierGetResDto;
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

    @PostMapping("/identifier")
    public UserIdentifierGetResDto getUserIdentifier(@RequestBody UserIdentifierGetReqDto userIdentifierGetReqDto){

        return new UserIdentifierGetResDto(userService.findByPhoneNo(userIdentifierGetReqDto.getPhoneNo()).getIdentifier());
    }
}
