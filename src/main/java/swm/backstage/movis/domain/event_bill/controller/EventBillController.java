package swm.backstage.movis.domain.event_bill.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event_bill.dto.*;
import swm.backstage.movis.domain.event_bill.service.EventBillService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/eventBill")
public class EventBillController {
    private final EventBillService eventBillService;
    /**
     * 지출 내역 추가 (알림으로)
     * */
    @PostMapping()
    public void createEventBill(@RequestBody EventBillCreateReqDto eventBillCreateReqDto) {
        eventBillService.createEventBill(eventBillCreateReqDto);
    }
    /**
     * 수동 분류
     * */
    @PatchMapping("/{eventBillId}")
    public void updateEventBill(@PathVariable("eventBillId") String eventBillId,
                                @RequestBody EventBillUpdateReqDto eventBillUpdateReqDto) {
        eventBillService.updateUnClassifiedEventBill(eventBillId, eventBillUpdateReqDto);
    }
    /**
     *  페이징 조회
     * */
    @GetMapping()
    public EventBillGetPagingListResDto getEventBillPagingList(@RequestParam(name = "eventId") String eventId,
                                                               @RequestParam LocalDateTime lastPaidAt,
                                                               @RequestParam(name = "lastId",defaultValue = "first") String lastId,
                                                               @RequestParam(name = "size",defaultValue = "20") int size) {
        return eventBillService.getEventBIllPagingList(eventId,lastPaidAt,lastId,size);
    }
    /**
     *  단일 조회
     * */
    @GetMapping("/{eventBillId}")
    public EventBillGetResDto getEventBill(@PathVariable String eventBillId) {
        return new EventBillGetResDto(eventBillService.getEventBillByUuid(eventBillId));
    }
    /**
     * 지출 내역 설명 추가
     * */
    @PostMapping("/explanation")
    public EventBillGetResDto createEventBillExplanation(@RequestBody EventBIllCreateExplanationReqDto  eventBIllCreateExplanationReqDto){
        return new EventBillGetResDto(eventBillService.createEventBillExplanation(eventBIllCreateExplanationReqDto));
    }

}
