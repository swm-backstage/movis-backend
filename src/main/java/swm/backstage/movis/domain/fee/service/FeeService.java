package swm.backstage.movis.domain.fee.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import swm.backstage.movis.domain.fee.dto.FeeCreateExplanationReqDto;
import swm.backstage.movis.domain.fee.dto.FeeInputReqDto;
import swm.backstage.movis.domain.fee.dto.FeeReqDto;
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
@Slf4j
public class FeeService {
    private final FeeRepository feeRepository;
    private final ClubService clubService;
    private final AccountBookService accountService;
    private final EventMemberService eventMemberService;
    private final EventService eventService;
    private final TransactionHistoryService transactionHistoryService;
    /**
     * Fee 등록 (직접 입력)
     * */
    @Transactional
    public void createFeeByInput(String eventId, FeeInputReqDto feeInputReqDto) {

        Event event = eventService.getEventByUuid(eventId);
        AccountBook accountBook = accountService.getAccountBookByClubId(event.getClub().getUlid());
        Fee fee;
        if(feeInputReqDto.getEventMemberId() == null){
            log.info("no memberId");
            fee = feeRepository.save(new Fee(feeInputReqDto,event.getClub(),event,null));
        }
        else{
            log.info("yes memberId {}", feeInputReqDto.getEventMemberId());
            EventMember eventMember = eventMemberService.getEventMemberByUuid(feeInputReqDto.getEventMemberId());
            fee = feeRepository.save(new Fee(feeInputReqDto,event.getClub(),event,eventMember));
        }
        accountBook.updateBalance(feeInputReqDto.getPaidAmount());
        accountBook.updateClassifiedDeposit(feeInputReqDto.getPaidAmount());
        event.updateBalance(feeInputReqDto.getPaidAmount());
        transactionHistoryService.saveTransactionHistory(TransactionHistoryCreateDto.fromFee(fee,Boolean.TRUE));
    }
    /**
     * Fee 등록 (알림)
     * */
    @Transactional
    public void createFeeByAlert(FeeReqDto feeReqDto) {
        Club club = clubService.getClubByUuId(feeReqDto.getClubId());
        // 미분류시
        if(feeReqDto.getEventMemberId() == null) {
            saveFee(feeReqDto, club, false, null);
        }
        //분류시
        else{
            saveFee(feeReqDto,club,true,null);
        }
    }

    @Transactional
    public void updateUnClassifiedFee(String feeId, FeeReqDto feeUpdateDto) {
        Club club = clubService.getClubByUuId(feeUpdateDto.getClubId());
        Fee fee = getFeeByUuId(feeId);
        saveFee(feeUpdateDto,club,true,fee);
    }

    public Fee getFeeByUuId(String feeId){
        return feeRepository.findByUlid(feeId).orElseThrow(()-> new BaseException("feeId is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    public FeeGetPagingListResDto getFeePagingList(String eventId, LocalDateTime localDateTime , String lastId, int size){
        Event event = eventService.getEventByUuid(eventId);
        List<Fee> feeList;
        if(lastId.equals("first")){
            feeList = feeRepository.getFirstPage(event.getUlid(),localDateTime,size+1);
        }
        else{
           Fee fee = getFeeByUuId(lastId);
            feeList = feeRepository.getNextPageByEventIdAndLastId(event.getUlid(),localDateTime,fee.getUlid(),size+1);
        }

        //하나 추가해서 조회한거 삭제해주기
        Boolean isLast = feeList.size() < size + 1;
        if(!isLast){
            feeList.remove(feeList.size()-1);
        }
        return new FeeGetPagingListResDto(feeList, isLast);
    }

    @Transactional
    protected void  saveFee(FeeReqDto feeReqDto, Club club, boolean isClassified, Fee fee) {
        // accountBook금액 수정 -> event 금액 수정 -> 회비 금액 저장 로직
        AccountBook accountBook = accountService.getAccountBookByClubId(feeReqDto.getClubId());
        if(!isClassified){
            fee = feeRepository.save(new Fee(feeReqDto,club));
            transactionHistoryService.saveTransactionHistory(TransactionHistoryCreateDto.fromFee(fee,Boolean.FALSE));
            accountBook.updateUnClassifiedDeposit(feeReqDto.getPaidAmount());
            accountBook.updateBalance(feeReqDto.getPaidAmount());
        }
        else{
            EventMember eventMember = eventMemberService.getEventMemberByUuid(feeReqDto.getEventMemberId());
            //자동 분류
            if(fee == null){
                fee = new Fee(feeReqDto, club, eventMember);
                feeRepository.save(fee);
                transactionHistoryService.saveTransactionHistory(TransactionHistoryCreateDto.fromFee(fee,Boolean.TRUE));
                accountBook.updateBalance(feeReqDto.getPaidAmount());
            }
            //수동분류
            else{
                fee.updateBlankElement(eventMember, feeReqDto);
                transactionHistoryService.updateTransactionHistory(new TransactionHistoryUpdateDto(fee.getUlid(),fee.getName(),fee.getEvent()));
                accountBook.updateUnClassifiedDeposit(-feeReqDto.getPaidAmount());
            }
            eventMember.updateEventMember();
            eventMember.getEvent().updateBalance(feeReqDto.getPaidAmount());
            accountBook.updateClassifiedDeposit(feeReqDto.getPaidAmount());
        }
    }
    /**
     * 설명 추가
     * */
    public Fee createFeeExplanation(FeeCreateExplanationReqDto reqDto) {
        Fee fee = getFeeByUuId(reqDto.getFeeId());
        fee.setExplanation(reqDto.getExplanation());
        return fee;
    }
    /**
     * 입금 여러개 isDeleted 수정
     * */
    @Transactional
    public int updateIsDeleted(String eventId){
        return feeRepository.updateIsDeletedByEventId(Boolean.TRUE,eventId);
    }
}