package swm.backstage.movis.domain.event_bill.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event_bill.dto.EventBillCreateDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillGetPagingListDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillUpdateDto;
import swm.backstage.movis.domain.event_bill.service.EventBillService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/eventBill")
public class EventBillController {
    private final EventBillService eventBillService;

    /**
     * 지출 내역 추가
     * */
    @PostMapping()
    public void createEventBill(@RequestBody EventBillCreateDto eventBillCreateDto) {
        eventBillService.createEventBill(eventBillCreateDto);
    }

    /**
     * 수동 분류
     * */
    @PatchMapping("/{eventBillId}")
    public void updateEventBill(@PathVariable("eventBillId") String eventBillId,
                                @RequestBody EventBillUpdateDto eventBillUpdateDto) {
        eventBillService.updateUnClassifiedEventBill(eventBillId,eventBillUpdateDto);
    }

    /**
     *  페이징 조회
     * */
    @GetMapping()
    public EventBillGetPagingListDto getEventBillPagingList(@RequestParam(name = "eventId") String eventId,
                                                        @RequestParam(name = "lastId",defaultValue = "first") String lastId,
                                                        @RequestParam(name = "size",defaultValue = "20") int size) {
        return eventBillService.getEventBIllPagingList(eventId,lastId,size);
    }
}
