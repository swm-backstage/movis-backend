package swm.backstage.movis.domain.club.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.club.dto.ClubCreateReqDto;
import swm.backstage.movis.domain.club.dto.ClubGetResDto;
import swm.backstage.movis.domain.club.dto.ClubGetListResDto;
import swm.backstage.movis.domain.club.service.ClubService;

@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping()
    public ClubGetResDto createClub(@RequestBody @Validated ClubCreateReqDto clubCreateReqDto) {
        return new ClubGetResDto(clubService.createClub(clubCreateReqDto));
    }
    @GetMapping("/{clubId}")
    public ClubGetResDto getClub(@PathVariable("clubId") String clubId){
        return new ClubGetResDto(clubService.getClubByUuId(clubId));
    }

    @GetMapping()
    public ClubGetListResDto getClubList(Authentication authentication){

        System.out.println("authentication.getPrincipal() = " + authentication.getPrincipal());
        return new ClubGetListResDto(clubService.getClubList());
    }
}
