package swm.backstage.movis.domain.event_member.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EventMemberListReqDto {
    private String eventId;
    private List<String> eventMemberIdList;
}
