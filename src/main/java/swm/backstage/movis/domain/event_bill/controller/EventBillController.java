package swm.backstage.movis.domain.event_bill.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * 회비 수동 입력
     * */
    @PreAuthorize("hasPermission(#eventId, 'eventId', {'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @PostMapping("/input")
    public void createEventBillByInput(@RequestParam("eventId") @Param("eventId") String eventId,
                                       @RequestBody EventBillInputReqDto dto){
        eventBillService.createEventBillByInput(eventId,dto);
    }
    /**
     * 지출 내역 추가 (알림으로)
     * */
    @PreAuthorize("hasPermission(#eventBillCreateReqDto.clubId, 'clubId', {'ROLE_MANAGER'})")
    @PostMapping()
    public void createEventBill(@RequestBody @Param("eventBillCreateReqDto") EventBillCreateReqDto eventBillCreateReqDto) {
        eventBillService.createEventBill(eventBillCreateReqDto);
    }
    /**
     * 수동 분류
     * */
    @PreAuthorize("hasPermission(#eventBillUpdateReqDto.clubId, 'clubId', {'ROLE_MANAGER'})")
    @PatchMapping("/{eventBillId}")
    public void updateEventBill(@PathVariable("eventBillId") String eventBillId,
                                @RequestBody @Param("eventBillUpdateReqDto") EventBillUpdateReqDto eventBillUpdateReqDto) {
        eventBillService.updateUnClassifiedEventBill(eventBillId, eventBillUpdateReqDto);
    }
    /**
     *  페이징 조회
     * */
    @PreAuthorize("hasPermission(#eventId, 'eventId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping()
    public EventBillGetPagingListResDto getEventBillPagingList(@RequestParam(name = "eventId") @Param("eventId") String eventId,
                                                               @RequestParam LocalDateTime lastPaidAt,
                                                               @RequestParam(name = "lastId",defaultValue = "first") String lastId,
                                                               @RequestParam(name = "size",defaultValue = "20") int size) {
        return eventBillService.getEventBIllPagingList(eventId,lastPaidAt,lastId,size);
    }
    /**
     *  단일 조회
     * */
    @PreAuthorize("hasPermission(#eventBillId, 'eventBillId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping("/{eventBillId}")
    public EventBillGetResDto getEventBill(@PathVariable @Param("eventBillId") String eventBillId) {
        return new EventBillGetResDto(eventBillService.getEventBillByUuid(eventBillId));
    }
    /**
     * 지출 내역 설명 추가
     * */
    @PreAuthorize("hasPermission(#EventBIllCreateExplanationReqDto.eventBillID, 'eventBillId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @PostMapping("/explanation")
    public EventBillGetResDto createEventBillExplanation(@RequestBody @Param("eventBIllCreateExplanationReqDto") EventBIllCreateExplanationReqDto  eventBIllCreateExplanationReqDto){
        return new EventBillGetResDto(eventBillService.createEventBillExplanation(eventBIllCreateExplanationReqDto));
    }

}
