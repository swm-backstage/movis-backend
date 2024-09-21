package swm.backstage.movis.domain.club_user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.auth.dto.AuthenticationPrincipalDetails;
import swm.backstage.movis.domain.club_user.ClubUser;
import swm.backstage.movis.domain.club_user.dto.ClubUserCreateReqDto;
import swm.backstage.movis.domain.club_user.dto.ClubUserGetResDto;
import swm.backstage.movis.domain.club_user.dto.ClubUserListGetResDto;
import swm.backstage.movis.domain.club_user.service.ClubUserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clubUsers")
public class ClubUserController {

    private final ClubUserService clubUserService;

    @PreAuthorize("hasPermission(#clubUserCreateReqDto.clubId, 'clubId', {'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @PostMapping
    public void createClubUser(@RequestBody @Param("clubUserCreateReqDto") ClubUserCreateReqDto clubUserCreateReqDto) {
        clubUserService.createClubUser(clubUserCreateReqDto);
    }

    @PreAuthorize("hasPermission(#clubId, 'clubId', {'ROLE_MANAGER'})")
    @PatchMapping("/{toIdentifier}")
    public void delegateRoleManagerToExecutive(@AuthenticationPrincipal AuthenticationPrincipalDetails principal,
                                               @RequestParam("clubId") @Param("clubId") String clubId,
                                               @PathVariable("toIdentifier") String toIdentifier) {
        clubUserService.delegateRoleManagerToExecutive(principal.getIdentifier(), toIdentifier, clubId);
    }

    @PreAuthorize("hasPermission(#clubId, 'clubId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping()
    public ClubUserListGetResDto getClubUserList(@RequestParam(name = "clubId") @Param("clubId") String clubId) {
        return new ClubUserListGetResDto(clubUserService.getClubUserList(clubId)
                .stream()
                .map(clubUser -> new ClubUserGetResDto(clubUser.getUuid(), clubUser.getIdentifier(), clubUser.getRole())).toList());
    }
}
