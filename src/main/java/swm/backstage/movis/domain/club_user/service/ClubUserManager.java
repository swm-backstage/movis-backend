package swm.backstage.movis.domain.club_user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.auth.enums.RoleType;
import swm.backstage.movis.domain.club_user.ClubUser;
import swm.backstage.movis.domain.club_user.repository.ClubUserRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubUserManager {

    private final ClubUserRepository clubUserRepository;

    public Integer getClubUserCnt(String identifier) {

        return clubUserRepository.countClubUserByIdentifier(identifier);
    }

    public void deleteClubUserByDeleteClub(List<ClubUser> clubUserList){

        // 모임에 1명의 운영진이라도 존재한다면, 총무 권한을 양도하거나 모든 운영진을 제명시켜야 한다.
        clubUserList.forEach(clubUser -> {
            if (clubUser.getRoleType().equals(RoleType.ROLE_EXECUTIVE) && !clubUser.getIsDeleted()) {
                throw new BaseException("총무 권한을 양도하거나 모든 운영진을 제명한 뒤에 모임 삭제를 진행해 주세요.", ErrorCode.UNAUTHORIZED_PERMISSION);
            }
            if (clubUser.getRoleType().equals(RoleType.ROLE_MANAGER)) {
                clubUser.updateIsDeleted(Boolean.TRUE);
            }
        });
    }
}
