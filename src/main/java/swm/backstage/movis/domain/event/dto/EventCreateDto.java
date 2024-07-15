package swm.backstage.movis.domain.event.dto;


import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventCreateDto {

    @NotNull
    private String clubId;

    @NotNull
    private String eventName;
}
