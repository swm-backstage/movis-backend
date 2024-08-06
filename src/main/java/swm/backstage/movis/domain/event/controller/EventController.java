package swm.backstage.movis.domain.event.controller;


import lombok.RequiredArgsConstructor;
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

    @PostMapping()
    public void createEvent(@RequestBody @Validated EventCreateReqDto eventCreateReqDto) {
        eventManager.createEvent(eventCreateReqDto);
    }

    @GetMapping("/{eventId}")
    public EventGetDto getEvent(@PathVariable(name = "eventId") String eventId) {
        return new EventGetDto(eventService.getEventByUuid(eventId));
    }

    @GetMapping()
    public EventGetPagingListResDto getEventPagingList(@RequestParam(name = "clubId") String clubId,
                                                       @RequestParam(name = "lastId",defaultValue = "first") String lastId,
                                                       @RequestParam(name = "size",defaultValue = "20") int size) {
        return eventService.getEventPagingList(clubId,lastId,size);
    }


    @PostMapping("/gatherFee")
    public void enrollGatherFee(@RequestParam("eventId") String eventId,
                                @RequestBody EventGatherFeeReqDto eventGatherFeeReqDto) {
        eventService.enrollGatherFee(eventId,eventGatherFeeReqDto);
    }

    @GetMapping("/funding")
    public EventGetFundingListResDto getEventFundingList(@RequestParam("clubId") String clubId,
                                                         @RequestParam("now") LocalDate now) {
        return new EventGetFundingListResDto(eventService.getCollectingMoneyEventList(clubId,now));
    }
}
