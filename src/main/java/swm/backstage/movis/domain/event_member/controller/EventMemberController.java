package swm.backstage.movis.domain.event_member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event_member.dto.EventMemberListReqDto;
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
    @PreAuthorize("hasPermission(#eventMemberListReqDto.eventId, 'eventId', {'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @PostMapping()
    public void addEventMembers(@RequestBody @Param("eventMemberListReqDto") EventMemberListReqDto eventMemberListReqDto){
        eventMemberService.addEventMembers(eventMemberListReqDto);
    }

    @GetMapping()
    @PreAuthorize("hasPermission(#eventId, 'eventId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    public EventMemberListResDto getEventMemberList(@RequestParam("eventId") @Param("eventId") String eventId){
        return new EventMemberListResDto(eventMemberService.getEventMemberList(eventId));
    }
}
