package swm.backstage.movis.domain.event.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event.dto.*;
import swm.backstage.movis.domain.event.service.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    @PostMapping()
    public void createEvent(@RequestBody @Validated EventCreateDto eventCreateDto) {
        eventService.createEvent(eventCreateDto);
    }

    @GetMapping("/{eventId}")
    public EventGetDto getEvent(@PathVariable(name = "eventId") String eventId) {
        return new EventGetDto(eventService.getEventByUuid(eventId));
    }

    @GetMapping()
    public EventGetListDto getEventPagingList(@RequestParam(name = "clubId") String clubId,
                                        @RequestParam(name = "lastId",defaultValue = "first") String lastId,
                                        @RequestParam(name = "size",defaultValue = "20") int size) {
        return eventService.getEventPagingList(clubId,lastId,size);
    }


    @PostMapping("/gatherFee")
    public void enrollGatherFee(@RequestBody EventGatherFeeReqDto eventGatherFeeReqDto) {
        eventService.enrollGatherFee(eventGatherFeeReqDto);
    }

    @GetMapping("/funding")
    public EventGetFundingListDto getEventFundingList(@RequestParam("clubId") String clubId,
                                                      @RequestParam("now") LocalDate now) {
        return new EventGetFundingListDto(eventService.getCollectingMoneyEventList(clubId,now));
    }
}
