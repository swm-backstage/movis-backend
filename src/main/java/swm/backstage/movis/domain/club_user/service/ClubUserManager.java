package swm.backstage.movis.domain.club_user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.club_user.repository.ClubUserRepository;

@Service
@RequiredArgsConstructor
public class ClubUserManager {

    private final ClubUserRepository clubUserRepository;

    public Integer getClubUserCnt(String identifier) {

        return clubUserRepository.countClubUserByIdentifier(identifier);
    }
}
