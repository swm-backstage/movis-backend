package swm.backstage.movis.domain.event_bill.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event_bill.dto.EventBillCreateReqDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillGetPagingListResDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillUpdateReqDto;
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
}
