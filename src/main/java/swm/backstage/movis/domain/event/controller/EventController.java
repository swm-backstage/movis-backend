package swm.backstage.movis.domain.event.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event.dto.EventCreateDto;
import swm.backstage.movis.domain.event.dto.EventGetDto;
import swm.backstage.movis.domain.event.dto.EventGetListDto;
import swm.backstage.movis.domain.event.service.EventService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
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
    public EventGetListDto getEventList(@RequestParam(name = "clubId") String clubId) {
        return new EventGetListDto(eventService.getEventList(clubId));
    }
}
