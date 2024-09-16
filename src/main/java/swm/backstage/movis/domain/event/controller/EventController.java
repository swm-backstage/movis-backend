package swm.backstage.movis.domain.event.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event.dto.*;
import swm.backstage.movis.domain.event.service.EventManager;
import swm.backstage.movis.domain.event.service.EventService;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;
    private final EventManager eventManager;

    @PreAuthorize("hasPermission(#eventCreateReqDto.clubId, 'clubId', {'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @PostMapping()
    public void createEvent(@RequestBody @Validated @Param("eventCreateReqDto") EventCreateReqDto eventCreateReqDto) {
        eventManager.createEvent(eventCreateReqDto);
    }

    @PreAuthorize("hasPermission(#eventId, 'eventId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping("/{eventId}")
    public EventGetDto getEvent(@PathVariable(name = "eventId") @Param("eventId") String eventId) {
        return new EventGetDto(eventService.getEventByUuid(eventId));
    }

    @PreAuthorize("hasPermission(#clubId, 'clubId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping()
    public EventGetPagingListResDto getEventPagingList(@RequestParam(name = "clubId") @Param("clubId") String clubId,
                                                       @RequestParam(name = "lastId",defaultValue = "first") String lastId,
                                                       @RequestParam(name = "size",defaultValue = "20") int size) {
        return eventService.getEventPagingList(clubId,lastId,size);
    }

    @PreAuthorize("hasPermission(#eventId, 'eventId', {'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @PostMapping("/gatherFee")
    public void enrollGatherFee(@RequestParam("eventId") @Param("eventId") String eventId,
                                @RequestBody EventGatherFeeReqDto eventGatherFeeReqDto) {
        eventService.enrollGatherFee(eventId,eventGatherFeeReqDto);
    }

    @PreAuthorize("hasPermission(#clubId, 'clubId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping("/funding")
    public EventGetFundingListResDto getEventFundingList(@RequestParam("clubId") String clubId,
                                                         @RequestParam("now") LocalDate now) {
        return new EventGetFundingListResDto(eventService.getCollectingMoneyEventList(clubId,now));
    }
    /**
     * 이벤트 수정
     * */
    @PreAuthorize("hasPermission(#eventId, 'eventId', {'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @PatchMapping()
    public EventGetDto updateEvent(@RequestParam(name = "eventId") String eventId,
                            @RequestBody EventUpdateReqDto eventUpdateReqDto) {
        return new EventGetDto(eventService.updateEvent(eventId,eventUpdateReqDto));
    }
    /**
     * 이벤트 삭제
     * */
    @PreAuthorize("hasPermission(#clubId, 'clubId', {'ROLE_MANAGER'})")
    @DeleteMapping()
    public void deleteEvent(@RequestParam(name = "clubId") String clubId,
                            @RequestParam(name = "eventId") String eventId) {
        eventService.deleteEvent(clubId,eventId);
    }

}
