package swm.backstage.movis.domain.user.controlloer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.auth.dto.AuthenticationPrincipalDetails;
import swm.backstage.movis.domain.user.dto.*;
import swm.backstage.movis.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserGetResDto getUserByToken(@AuthenticationPrincipal AuthenticationPrincipalDetails principal){

        return new UserGetResDto(userService.findByIdentifier(principal.getIdentifier()));
    }

    @PostMapping("/identifier")
    public UserIdentifierGetResDto getUserIdentifier(@RequestBody @Validated UserIdentifierGetReqDto userIdentifierGetReqDto){

        return new UserIdentifierGetResDto(userService.findByPhoneNo(userIdentifierGetReqDto.getPhoneNo()).getIdentifier());
    }

    @PatchMapping("/password")
    public void updateUserPassword(@AuthenticationPrincipal AuthenticationPrincipalDetails principal,
                                   @RequestBody @Validated UserPasswordUpdateReqDto userPasswordUpdateReqDto){

        userService.updatePassword(principal.getIdentifier(), userPasswordUpdateReqDto.getOldPassword(), userPasswordUpdateReqDto.getNewPassword());
    }

    @PostMapping("/password/reset")
    public void resetUserPassword(@RequestBody UserPasswordResetReqDto userPasswordResetReqDto){
        userService.resetPassword(userPasswordResetReqDto.getPhoneNo());
    }

    @PatchMapping("/me")
    public void deleteUser(@AuthenticationPrincipal AuthenticationPrincipalDetails principal,
                           @RequestBody @Validated UserDeleteReqDto userDeleteReqDto){

        userService.deleteUser(principal.getIdentifier(), userDeleteReqDto.getPassword());
    }
}
