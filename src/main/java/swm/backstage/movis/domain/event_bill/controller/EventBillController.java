package swm.backstage.movis.domain.event_bill.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event_bill.dto.EventBillCreateDto;
import swm.backstage.movis.domain.event_bill.service.EventBillService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/eventBill")
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
//    @PatchMapping()
//    public SuccessCode updateEventBill(@RequestBody EventBillCreateDto eventBillCreateDto) {
//
//    }
}
