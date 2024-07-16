package swm.backstage.movis.domain.fee.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.accout_book.service.AccountBookService;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.event_member.service.EventMemberService;
import swm.backstage.movis.domain.fee.Fee;
import swm.backstage.movis.domain.fee.dto.FeeDto;
import swm.backstage.movis.domain.fee.repository.FeeRepository;

@Service
@RequiredArgsConstructor
public class FeeService {
    private final FeeRepository feeRepository;
    private final ClubService clubService;
    private final AccountBookService accountService;
    private final EventMemberService eventMemberService;


    /**
     * 분류된 요소와 분류되지 않은 요소만 전달 된다.
     * -> EventMember의 걷을 금액을 따라서 분류가 된 회비라면 0으로 만들고 납부여부를 true로 해야함
     * */
    @Transactional
    public void createFee(FeeDto feeDto) {
        Club club = clubService.getClubByUuId(feeDto.getClubId());

        // 미분류시
        if(feeDto.getEventMemberId() == null) {
            feeRepository.save(new Fee(feeDto,club));
        }
        //분류시
        else{
            saveFee(feeDto,club);
        }
    }

    @Transactional
    public void updateUnClassifiedFee(FeeDto feeUpdateDto) {
        Club club = clubService.getClubByUuId(feeUpdateDto.getClubId());
        saveFee(feeUpdateDto,club);
    }

    @Transactional
    protected void saveFee(FeeDto feeDto, Club club) {
        AccountBook accountBook = accountService.getAccountBookByClub(clubService.getClubByUuId(feeDto.getClubId()));
        // accountBook금액 수정 -> event 금액 수정 -> 회비 금액 저장 로직
        EventMember eventMember = eventMemberService.getEventMemberByUuid(feeDto.getEventMemberId());
        feeRepository.save(new Fee(feeDto, club, eventMember));
        eventMember.updateEventMember();
        eventMember.getEvent().updateBalance(feeDto.getPaidAmount());
        accountBook.updateBalance(feeDto.getPaidAmount());
    }

}
