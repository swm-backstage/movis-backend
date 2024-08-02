package swm.backstage.movis.domain.event.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventCreateReqDto {

    @NotNull
    private String clubId;

    @NotNull
    private String eventName;
}
