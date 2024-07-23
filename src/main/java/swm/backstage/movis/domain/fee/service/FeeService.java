package swm.backstage.movis.domain.fee.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.accout_book.service.AccountBookService;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.event_member.service.EventMemberService;
import swm.backstage.movis.domain.fee.Fee;
import swm.backstage.movis.domain.fee.dto.FeeDto;
import swm.backstage.movis.domain.fee.dto.FeeGetPagingListResDto;
import swm.backstage.movis.domain.fee.repository.FeeRepository;
import swm.backstage.movis.domain.transaction_history.dto.TransactionHistoryCreateDto;
import swm.backstage.movis.domain.transaction_history.dto.TransactionHistoryUpdateDto;
import swm.backstage.movis.domain.transaction_history.service.TransactionHistoryService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeeService {
    private final FeeRepository feeRepository;
    private final ClubService clubService;
    private final AccountBookService accountService;
    private final EventMemberService eventMemberService;
    private final EventService eventService;
    private final TransactionHistoryService transactionHistoryService;


    /**
     * Fee 등록 (알림)
     * */
    @Transactional
    public void createFee(FeeDto feeDto) {
        Club club = clubService.getClubByUuId(feeDto.getClubId());
        // 미분류시
        if(feeDto.getEventMemberId() == null) {
            saveFee(feeDto, club, false, null);
        }
        //분류시
        else{
            saveFee(feeDto,club,true,null);
        }
    }

    @Transactional
    public void updateUnClassifiedFee(String feeId, FeeDto feeUpdateDto) {
        Club club = clubService.getClubByUuId(feeUpdateDto.getClubId());
        Fee fee = getFeeById(feeId);
        saveFee(feeUpdateDto,club,true,fee);
    }

    public Fee getFeeById(String feeId){
        return feeRepository.findByUuid(feeId).orElseThrow(()-> new BaseException("feeId is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    public FeeGetPagingListResDto getFeePagingList(String eventId, LocalDateTime localDateTime , String lastId, int size){
        Event event = eventService.getEventByUuid(eventId);
        List<Fee> feeList;
        if(lastId.equals("first")){
            feeList = feeRepository.getFirstPage(event.getId(),localDateTime,size+1);
        }
        else{
           Fee fee = getFeeById(lastId);
            feeList = feeRepository.getNextPageByEventIdAndLastId(event.getId(),localDateTime,fee.getId(),size+1);
        }

        //하나 추가해서 조회한거 삭제해주기
        Boolean isLast = feeList.size() < size + 1;
        if(!isLast){
            feeList.remove(feeList.size()-1);
        }
        return new FeeGetPagingListResDto(feeList, isLast);
    }

    @Transactional
    protected void saveFee(FeeDto feeDto, Club club,boolean isClassified,Fee fee) {
        if(!isClassified){
            fee = feeRepository.save(new Fee(UUID.randomUUID().toString(),feeDto,club));
            transactionHistoryService.saveTransactionHistory(TransactionHistoryCreateDto.fromFee(fee,Boolean.FALSE));
        }
        else{
            // accountBook금액 수정 -> event 금액 수정 -> 회비 금액 저장 로직
            AccountBook accountBook = accountService.getAccountBookByClubId(feeDto.getClubId());
            EventMember eventMember = eventMemberService.getEventMemberByUuid(feeDto.getEventMemberId());
            //자동 분류
            if(fee == null){
                fee = new Fee(UUID.randomUUID().toString(), feeDto, club, eventMember);
                feeRepository.save(fee);
                transactionHistoryService.saveTransactionHistory(TransactionHistoryCreateDto.fromFee(fee,Boolean.TRUE));
            }
            //수동분류
            else{
                fee.updateBlankElement(eventMember,feeDto);
                transactionHistoryService.updateTransactionHistory(new TransactionHistoryUpdateDto(fee.getUuid(),fee.getName(),fee.getEvent()));
            }
            eventMember.updateEventMember();
            eventMember.getEvent().updateBalance(feeDto.getPaidAmount());
            accountBook.updateBalance(feeDto.getPaidAmount());
        }
    }

}