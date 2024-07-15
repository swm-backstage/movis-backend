package swm.backstage.movis.domain.club.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.dto.ClubCreateDto;
import swm.backstage.movis.domain.club.repository.ClubRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;

    @Transactional
    public Club createClub(ClubCreateDto clubCreateDto) {
        return clubRepository.save(new Club(clubCreateDto,UUID.randomUUID().toString(),new AccountBook(clubCreateDto.getBalance())));
    }

    /**
     * NULL 허용 X
     * */
    public Club getClubByUuId(String id) throws BaseException {
        return clubRepository.findByUuidAndIsDeleted(id,Boolean.FALSE).orElseThrow(()->new BaseException("clubId is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    /**
     * NULL 허용
     * */
    public Club findClubByUuId(String id) throws BaseException {
        return clubRepository.findByUuidAndIsDeleted(id,Boolean.FALSE).orElse(null);
    }

    public List<Club> getClubList(){
        return clubRepository.findAll();
    }

}
