package swm.backstage.movis.domain.transaction_history.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.event.Event;

@Getter
@NoArgsConstructor
public class TransactionHistoryUpdateDto {
    private String elementId;
    private String name;
    private Event event;

    public TransactionHistoryUpdateDto(String elementId, String name, Event event) {
        this.elementId = elementId;
        this.name = name;
        this.event = event;
    }
}
