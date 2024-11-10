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
import swm.backstage.movis.domain.user.service.UserManager;
import swm.backstage.movis.domain.user.service.UserService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClubUserService {

    private final UserManager userManager;
    private final ClubService clubService;
    private final ClubUserRepository clubUserRepository;


    /**
     * 소프트 삭제된 ClubUser에 대하여 또다시 추가되는 경우
     *  1. 새로운 row를 추가(ClubUser의 unique 제약 조건으로 인해 불가능)
     *  2. 삭제 상태를 ture -> false로 변경(현재 채택한 방식)
     * */
    @Transactional
    public void createClubUser(ClubUserCreateReqDto clubUserCreateReqDto) {

        Club club = clubService.findClubByUuId(clubUserCreateReqDto.getClubId());
        User user = userManager.findByPhoneNo(clubUserCreateReqDto.getPhoneNo());
        Optional<ClubUser> optionalClubUser = clubUserRepository.findByIdentifierAndClub_Ulid(user.getIdentifier(), club.getUlid());

        if (optionalClubUser.isPresent()) {
            ClubUser clubUser = optionalClubUser.get();
            if (!clubUser.getIsDeleted()) {
                throw new BaseException("해당 클럽에 이미 등록된 사용자입니다.", ErrorCode.DUPLICATE_CLUB_USER);
            }
            clubUser.updateIsDeleted(false);
        } else {
            ClubUser newClubUser = new ClubUser(UUID.randomUUID().toString(), RoleType.ROLE_EXECUTIVE, user, club);
            clubUserRepository.save(newClubUser);
        }
    }

    @Transactional
    public void delegateRoleManagerToExecutive(String fromIdentifier, String toIdentifier, String clubId) {

        if (fromIdentifier.equals(toIdentifier)) {

            throw new BaseException("자기 자신에게 권한을 위임할 수 없습니다. ", ErrorCode.INTERNAL_SERVER_ERROR);
        }
        ClubUser fromClubUser = clubUserRepository.findByIdentifierAndClub_UlidAndIsDeleted(fromIdentifier, clubId, Boolean.FALSE)
                .orElseThrow(() -> new BaseException("Element Not Found", ErrorCode.ELEMENT_NOT_FOUND));
        ClubUser toClubUser = clubUserRepository.findByIdentifierAndClub_UlidAndIsDeleted(toIdentifier, clubId, Boolean.FALSE)
                .orElseThrow(() -> new BaseException("위임할 운영진이 존재하지 않습니다. ", ErrorCode.ELEMENT_NOT_FOUND));

        fromClubUser.updateRole(RoleType.ROLE_EXECUTIVE);
        toClubUser.updateRole(RoleType.ROLE_MANAGER);

        clubUserRepository.save(fromClubUser);
        clubUserRepository.save(toClubUser);
    }

    public ClubUser getClubUser(String identifier, String clubId) {

        return clubUserRepository.findByIdentifierAndClub_UlidAndIsDeleted(identifier, clubId, Boolean.FALSE)
                .orElseThrow(() -> new BaseException("clubUser is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    public List<ClubUser> getClubUserList(String clubId) {

        return clubUserRepository.findAllByClub_UlidAndIsDeleted(clubId, Boolean.FALSE);
    }

    @Transactional
    public void deleteClubUser(String identifier, String clubId) {

        ClubUser clubUser = this.getClubUser(identifier, clubId);
        clubUser.updateIsDeleted(Boolean.TRUE);
    }

    @Transactional
    public void deleteClubUserByManager(String actorIdentifier, String targetIdentifier, String clubId) {

        if(actorIdentifier.equals(targetIdentifier)){

            throw new BaseException("총무는 자기 자신을 제명시킬 수 없습니다. ", ErrorCode.UNAUTHORIZED_PERMISSION);
        }

        this.deleteClubUser(targetIdentifier, clubId);
    }
}
