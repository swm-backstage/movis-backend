package swm.backstage.movis.domain.club.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.auth.RoleType;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.dto.ClubCreateReqDto;
import swm.backstage.movis.domain.club.repository.ClubRepository;
import swm.backstage.movis.domain.club_user.ClubUser;
import swm.backstage.movis.domain.user.User;
import swm.backstage.movis.domain.user.service.UserService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final UserService userService;

    @Transactional
    public Club createClub(ClubCreateReqDto clubCreateReqDto,String identifier) {
        User user = userService.findByIdentifier(identifier).orElseThrow(()-> new BaseException("Element Not Found",ErrorCode.ELEMENT_NOT_FOUND));
        Club club = new Club(clubCreateReqDto, UUID.randomUUID().toString(), new AccountBook());
        ClubUser clubUser = new ClubUser(UUID.randomUUID().toString(), RoleType.ROLE_EXECUTIVE.value(), user, club);
        return clubRepository.save(club);
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

    public List<Club> getClubList(String identifier){
        User user = userService.findByIdentifier(identifier).orElseThrow(()-> new BaseException("Element Not Found",ErrorCode.ELEMENT_NOT_FOUND));
        List<ClubUser> clubUserList = user.getClubUserList();
        return clubUserList.stream()
                .map(ClubUser::getClub)
                .collect(Collectors.toList());
    }

    public String getClubUid(String accountNumber, String identifier) {
        User user = userService.findByIdentifier(identifier).orElseThrow(()-> new BaseException("Element Not Found",ErrorCode.ELEMENT_NOT_FOUND));
        List<ClubUser> clubUserList = user.getClubUserList();
        return clubUserList.stream()
                .filter(clubUser -> clubUser.getClub().getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new BaseException("club을 찾을 수 없습니다.",ErrorCode.ELEMENT_NOT_FOUND))
                .getUuid();
    }
}
