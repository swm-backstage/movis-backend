package swm.backstage.movis.domain.event_bill.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event_bill.dto.EventBillCreateDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillUpdateDto;
import swm.backstage.movis.domain.event_bill.service.EventBillService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/eventBill")
public class EventBillController {
    private final EventBillService eventBillService;

    @Value("${amazon.aws.s3.bucket}")
    private String bucketName;

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

    // presigned url 생성
    @GetMapping("/url-generate")
    public String generatePresignedUrl(@RequestParam String billUid, @RequestParam String extension) {
        return eventBillService.generatePreSignUrl(billUid + "." + extension, bucketName);
    }
}
