package swm.backstage.movis.domain.club.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.auth.dto.AuthenticationPrincipalDetails;
import swm.backstage.movis.domain.club.dto.ClubCreateReqDto;
import swm.backstage.movis.domain.club.dto.ClubGetResDto;
import swm.backstage.movis.domain.club.dto.ClubGetListResDto;
import swm.backstage.movis.domain.club.dto.ClubGetUidResDto;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.user.service.UserService;


@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    private final UserService userService;

    @PostMapping()
    public ClubGetResDto createClub(@AuthenticationPrincipal AuthenticationPrincipalDetails principal,
                                    @RequestBody @Validated ClubCreateReqDto clubCreateReqDto) {
        return new ClubGetResDto(clubService.createClub(clubCreateReqDto, principal.getIdentifier()));
    }
    @GetMapping("/{clubId}")
    public ClubGetResDto getClub(@PathVariable("clubId") String clubId){
        return new ClubGetResDto(clubService.getClubByUuId(clubId));
    }

    @GetMapping()
    public ClubGetListResDto getClubList(@AuthenticationPrincipal AuthenticationPrincipalDetails principal){
        return new ClubGetListResDto(clubService.getClubList(principal.getIdentifier()));
    }

    @GetMapping("/forAlert")
    public ClubGetUidResDto getClubUid(@AuthenticationPrincipal AuthenticationPrincipalDetails principal,
                                       @RequestParam("accountNumber") String accountNumber){
        return new ClubGetUidResDto(clubService.getClubUid(accountNumber, principal.getIdentifier()));
    }
}
