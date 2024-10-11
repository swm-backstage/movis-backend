package swm.backstage.movis.domain.event.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EventCreateReqDto {

    @NotNull
    private String clubId;

    @NotNull
    private String eventName;

    private EventGatherFeeReqDto gatherFeeInfo;

    private List<String> eventMemberIdList;

    public EventCreateReqDto(String clubId, String eventName, EventGatherFeeReqDto gatherFeeInfo, List<String> eventMemberIdList) {
        this.clubId = clubId;
        this.eventName = eventName;
        this.gatherFeeInfo = gatherFeeInfo;
        this.eventMemberIdList = eventMemberIdList;
    }
}
