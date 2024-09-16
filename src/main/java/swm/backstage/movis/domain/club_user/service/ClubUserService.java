package swm.backstage.movis.domain.club_user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.club_user.ClubUser;
import swm.backstage.movis.domain.club_user.dto.ClubUserCreateReqDto;
import swm.backstage.movis.domain.club_user.repository.ClubUserRepository;
import swm.backstage.movis.domain.auth.RoleType;
import swm.backstage.movis.domain.user.User;
import swm.backstage.movis.domain.user.service.UserService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClubUserService {

    private final UserService userService;
    private final ClubService clubService;
    private final ClubUserRepository clubUserRepository;

    public void createClubUser(ClubUserCreateReqDto clubUserCreateReqDto) {
        Club club = clubService.findClubByUuId(clubUserCreateReqDto.getClubId());
        User user = userService.findByIdentifier(clubUserCreateReqDto.getIdentifier()).orElseThrow(()-> new BaseException("Element Not Found", ErrorCode.ELEMENT_NOT_FOUND));
        ClubUser clubUser = new ClubUser(UUID.randomUUID().toString(), RoleType.ROLE_EXECUTIVE.value(), user, club);
        clubUserRepository.save(clubUser);
    }

    public ClubUser getClubUser(String identifier, String clubId) {
        return clubUserRepository.findByIdentifierAndClub_Uuid(identifier, clubId).orElseThrow(() -> new BaseException("clubUser is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }
}
