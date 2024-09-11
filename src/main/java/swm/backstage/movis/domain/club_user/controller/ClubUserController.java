package swm.backstage.movis.domain.club_user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.auth.dto.AuthenticationPrincipalDetails;
import swm.backstage.movis.domain.club_user.dto.ClubUserCreateReqDto;
import swm.backstage.movis.domain.club_user.dto.ClubUserGetResDto;
import swm.backstage.movis.domain.club_user.service.ClubUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clubUsers")
public class ClubUserController {

    private ClubUserService clubUserService;

    @PostMapping
    public void createClubUser(@RequestBody ClubUserCreateReqDto clubUserCreateReqDto) {
        clubUserService.createClubUser(clubUserCreateReqDto);
    }

    @GetMapping()
    public ClubUserGetResDto getClubUser(@AuthenticationPrincipal AuthenticationPrincipalDetails principal,
                                         @RequestParam(name = "clubId") String clubId){
        return new ClubUserGetResDto(clubUserService.getClubUser(principal.getIdentifier(), clubId));
    }
}
