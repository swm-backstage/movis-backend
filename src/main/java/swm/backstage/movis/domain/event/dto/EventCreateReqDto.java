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
}
