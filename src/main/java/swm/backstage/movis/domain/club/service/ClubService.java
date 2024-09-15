package swm.backstage.movis.domain.club.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.dto.ClubCreateReqDto;
import swm.backstage.movis.domain.club.dto.CodeType;
import swm.backstage.movis.domain.club.repository.ClubRepository;
import swm.backstage.movis.domain.user.User;
import swm.backstage.movis.domain.user.service.UserService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final UserService userService;

    @Transactional
    public Club createClub(ClubCreateReqDto clubCreateReqDto,String identifier) {
        User user = userService.findByIdentifier(identifier).orElseThrow(()-> new BaseException("Element Not Found",ErrorCode.ELEMENT_NOT_FOUND));
        Club newClub = new Club(clubCreateReqDto,UUID.randomUUID().toString(),new AccountBook(),user);

        newClub.setEntryCode(createRandomCode());
        newClub.setInviteCode(createRandomCode());
        return clubRepository.save(newClub);
    }

    /**
     * NULL 허용 X
     * */
    public Club getClubByUuId(String id) throws BaseException {
        return clubRepository.findByUuidAndIsDeleted(id,Boolean.FALSE).orElseThrow(()->new BaseException("clubId is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    public String getClubUuidByEntryCode(String entryCode) {
        return clubRepository.findByEntryCode(entryCode)
                .orElseThrow(() -> new BaseException("club을 찾을 수 없습니다.",ErrorCode.ELEMENT_NOT_FOUND)).getUuid();
    }

    public String getClubUuidByInviteCode(String inviteCode) {
        return clubRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new BaseException("club을 찾을 수 없습니다.",ErrorCode.ELEMENT_NOT_FOUND)).getUuid();
    }

    /**
     * NULL 허용
     * */
    public Club findClubByUuId(String id) throws BaseException {
        return clubRepository.findByUuidAndIsDeleted(id,Boolean.FALSE).orElse(null);
    }

    public List<Club> getClubList(String identifier){
        User user = userService.findByIdentifier(identifier).orElseThrow(()-> new BaseException("Element Not Found",ErrorCode.ELEMENT_NOT_FOUND));
        return clubRepository.findAllByUser_Id(user.getId());
    }

    public String getClubUid(String accountNumber, String identifier) {
        User user = userService.findByIdentifier(identifier).orElseThrow(()-> new BaseException("Element Not Found",ErrorCode.ELEMENT_NOT_FOUND));
        return clubRepository.findByUser_IdAndAccountNumber(user.getId(), accountNumber)
                .orElseThrow(() -> new BaseException("club을 찾을 수 없습니다.",ErrorCode.ELEMENT_NOT_FOUND)).getUuid();
    }

    // 입장 코드
    public String updateEntryCode(String clubId) {
        return updateCode(clubId, CodeType.ENTRY);
    }

    // 초대 코드
    public String updateInviteCode(String clubId) {
        return updateCode(clubId, CodeType.INVITE);
    }

    public String updateCode(String clubId, CodeType codeType) {
        Club club = clubRepository.findByUuidAndIsDeleted(clubId, Boolean.FALSE)
                .orElseThrow(() -> new BaseException("club을 찾을 수 없습니다.", ErrorCode.ELEMENT_NOT_FOUND));

        String randomCode = createRandomCode();

        if (codeType == CodeType.ENTRY) {
            club.setEntryCode(randomCode);
        } else if (codeType == CodeType.INVITE) {
            club.setInviteCode(randomCode);
        }

        clubRepository.save(club);
        return randomCode;
    }

    // TODO : 랜덤코드 로직의 효율성 더 고려해볼 것
    private String createRandomCode() {
        String randomCode = "";
        for (int i = 0; i < 5; i++) {
            int random = (int) (Math.random() * 2);
            if (random == 0) {
                randomCode += (char) ((int) (Math.random() * 26) + 65);
            } else {
                randomCode += (int) (Math.random() * 10);
            }
        }
        return randomCode;
    }
}
