package swm.backstage.movis.domain.event_bill.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.accout_book.AccountBook;
import swm.backstage.movis.domain.accout_book.service.AccountBookService;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.event_bill.EventBill;
import swm.backstage.movis.domain.event_bill.dto.EventBillCreateDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillGetPagingListDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillUpdateDto;
import swm.backstage.movis.domain.event_bill.repository.EventBillRepository;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventBillService {
    private final EventBillRepository eventBillRepository;
    private final ClubService clubService;
    private final AccountBookService accountBookService;
    private final EventService eventService;
    private final AmazonS3 amazonS3;

    @Transactional
    public void createEventBill(EventBillCreateDto eventBillCreateDto){
        eventBillRepository.save(new EventBill(UUID.randomUUID().toString(),eventBillCreateDto,clubService.getClubByUuId(eventBillCreateDto.getClubId())));
    }

    public EventBill getEventBillByUuid(String eventBillId){
        return eventBillRepository.findByUuid(eventBillId).orElseThrow(()-> new BaseException("eventBill is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    @Transactional
    public void updateUnClassifiedEventBill(String eventBillId, EventBillUpdateDto eventBillUpdateDto){
        AccountBook accountBook = accountBookService.getAccountBookByClubId(eventBillUpdateDto.getClubId());
        EventBill eventBill = getEventBillByUuid(eventBillId);
        Event event = eventService.getEventByUuid(eventBillUpdateDto.getEventId());
        accountBook.updateBalance(eventBill.getAmount());
        event.updateBalance(eventBill.getAmount());
        eventBill.setEvent(event);
    }


    // AWS S3 presigned url 생성
    // 반환되는 주소로 PUT 요청을 보내면 파일 업로드 가능
    // 영수증 이미지는 다음으로 조회 가능 -> https://movis-bucket.s3.ap-northeast-2.amazonaws.com/billImage/이미지명.확장자명
    public String generatePreSignUrl(String fileName, String bucketName) {
        String filePath = "billImage/" + fileName; // 'billImage/' 폴더에 파일 저장
        HttpMethod httpMethod = HttpMethod.PUT; // 파일 업로드를 위한 HTTP 메소드

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); // 10분간 유효
        return amazonS3.generatePresignedUrl(bucketName, filePath, calendar.getTime(), httpMethod).toString();
    }
    
    public EventBillGetPagingListDto getEventBIllPagingList(String eventId, String lastId, int size){
        Event event = eventService.getEventByUuid(eventId);
        List<EventBill> eventBillList;
        if(lastId.equals("first")){
            eventBillList = eventBillRepository.getFirstPage(event.getId(),size+1);
        }
        else{
            EventBill eventBill = getEventBillByUuid(lastId);
            eventBillList = eventBillRepository.getNextPageByEventIdAndLastId(event.getId(),eventBill.getId(),size+1);
        }

        //하나 추가해서 조회한거 삭제해주기
        Boolean isLast = eventBillList.size() < size + 1;
        if(!isLast){
            eventBillList.remove(eventBillList.size()-1);
        }
        return new EventBillGetPagingListDto(eventBillList, isLast);
        
    }
}
