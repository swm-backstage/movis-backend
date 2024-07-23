package swm.backstage.movis.domain.event_member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event_member.EventMember;

import java.util.List;

@Getter
@NoArgsConstructor
public class EventMemberListResDto {
    List<EventMemberListElementDto> eventMemberList;
    public EventMemberListResDto(List<EventMember> eventMemberList) {
        this.eventMemberList = eventMemberList.stream()
                .map(EventMemberListElementDto::new).toList();
    }
}
