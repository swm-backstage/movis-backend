package swm.backstage.movis.domain.alert.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AlertGetRequestDTO {
    private String name;
    private String cludUid;
    private String contents;
    private Long cash;
    private BillType billType;
    private LocalDateTime createdAt;
}
