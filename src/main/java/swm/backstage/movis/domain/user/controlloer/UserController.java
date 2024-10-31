package swm.backstage.movis.domain.user.controlloer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.auth.dto.AuthenticationPrincipalDetails;
import swm.backstage.movis.domain.user.dto.UserGetResDto;
import swm.backstage.movis.domain.user.dto.UserIdentifierGetReqDto;
import swm.backstage.movis.domain.user.dto.UserIdentifierGetResDto;
import swm.backstage.movis.domain.user.dto.UserPasswordUpdateReqDto;
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

    @DeleteMapping("/me")
    public void deleteUser(@AuthenticationPrincipal AuthenticationPrincipalDetails principal){

        userService.deleteUser(principal.getIdentifier());
    }
}
