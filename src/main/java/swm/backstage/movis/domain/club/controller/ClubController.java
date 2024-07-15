package swm.backstage.movis.domain.club.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.club.dto.ClubCreateDto;
import swm.backstage.movis.domain.club.dto.ClubGetDto;
import swm.backstage.movis.domain.club.dto.ClubGetListDto;
import swm.backstage.movis.domain.club.service.ClubService;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping()
    public ClubGetDto createClub(@RequestBody @Validated ClubCreateDto clubCreateDto) {
        return new ClubGetDto(clubService.createClub(clubCreateDto));
    }
    @GetMapping("/{clubId}")
    public ClubGetDto getClub(@PathVariable("clubId") String clubId){
        return new ClubGetDto(clubService.getClubByUuId(clubId));
    }

    @GetMapping()
    public ClubGetListDto getClubList(){
        return new ClubGetListDto(clubService.getClubList());
    }
}
