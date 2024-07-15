package swm.backstage.movis.domain.event_member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.backstage.movis.domain.event_member.dto.EventMemberListDto;
import swm.backstage.movis.domain.event_member.service.EventMemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/eventMembers")
public class EventMemberController {

    private final EventMemberService eventMemberService;

    /**
     *  이벤트 회원 추가
     * */
    @PostMapping()
    public void addEventMembers(@RequestBody EventMemberListDto eventMemberListDto){
        eventMemberService.addEventMembers(eventMemberListDto);
    }
}
