package swm.backstage.movis.domain.club.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.repository.ClubRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

@Service
@RequiredArgsConstructor
public class ClubManager {

    private final ClubRepository clubRepository;
    /**
     * NULL 허용 X
     * */
    public Club getClubByUuId(String id) throws BaseException {
        return clubRepository.findByUlidAndIsDeleted(id,Boolean.FALSE).orElseThrow(()->new BaseException("clubId is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }
}
