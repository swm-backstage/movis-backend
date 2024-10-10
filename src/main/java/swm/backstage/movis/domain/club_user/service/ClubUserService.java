package swm.backstage.movis.domain.club_user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.club_user.ClubUser;
import swm.backstage.movis.domain.club_user.dto.ClubUserCreateReqDto;
import swm.backstage.movis.domain.club_user.repository.ClubUserRepository;
import swm.backstage.movis.domain.auth.enums.RoleType;
import swm.backstage.movis.domain.user.User;
import swm.backstage.movis.domain.user.service.UserService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClubUserService {

    private final UserService userService;
    private final ClubService clubService;
    private final ClubUserRepository clubUserRepository;

    @Transactional
    public void createClubUser(ClubUserCreateReqDto clubUserCreateReqDto) {
        Club club = clubService.findClubByUuId(clubUserCreateReqDto.getClubId());
        User user = userService.findByIdentifier(clubUserCreateReqDto.getIdentifier()).orElseThrow(()-> new BaseException("Element Not Found", ErrorCode.ELEMENT_NOT_FOUND));
        ClubUser clubUser = new ClubUser(UUID.randomUUID().toString(), RoleType.ROLE_EXECUTIVE, user, club);
        clubUserRepository.save(clubUser);
    }

    @Transactional
    public void delegateRoleManagerToExecutive(String fromIdentifier, String toIdentifier, String clubId) {
        if (fromIdentifier.equals(toIdentifier)) {
            throw new BaseException("자기 자신에게 권한을 위임할 수 없습니다. ", ErrorCode.INTERNAL_SERVER_ERROR);
        }
        ClubUser fromClubUser = clubUserRepository.findByIdentifierAndClub_Ulid(fromIdentifier, clubId)
                .orElseThrow(() -> new BaseException("Element Not Found", ErrorCode.ELEMENT_NOT_FOUND));
        ClubUser toClubUser = clubUserRepository.findByIdentifierAndClub_Ulid(toIdentifier, clubId)
                .orElseThrow(() -> new BaseException("Element Not Found", ErrorCode.ELEMENT_NOT_FOUND));

        fromClubUser.updateRole(RoleType.ROLE_EXECUTIVE);
        toClubUser.updateRole(RoleType.ROLE_MANAGER);

        clubUserRepository.save(fromClubUser);
        clubUserRepository.save(toClubUser);
    }

    public ClubUser getClubUser(String identifier, String clubId) {
        return clubUserRepository.findByIdentifierAndClub_Ulid(identifier, clubId).orElseThrow(() -> new BaseException("clubUser is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    public List<ClubUser> getClubUserList(String clubId) {
        return clubUserRepository.findAllByClub_Ulid(clubId);
    }
}
