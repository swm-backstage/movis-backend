package swm.backstage.movis.domain.event_member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event_member.dto.EventMemberListDto;
import swm.backstage.movis.domain.event_member.dto.EventMemberListResDto;
import swm.backstage.movis.domain.event_member.service.EventMemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/eventMembers")
public class EventMemberController {

    private final EventMemberService eventMemberService;

    /**
     *  이벤트 회원 추가
     * */
    @PostMapping()
    public void addEventMembers(@RequestBody EventMemberListDto eventMemberListDto){
        eventMemberService.addEventMembers(eventMemberListDto);
    }

    @GetMapping()
    public EventMemberListResDto getEventMemberList(@RequestParam("eventId") String eventId){
        return new EventMemberListResDto(eventMemberService.getEventMemberList(eventId));
    }
}
