package swm.backstage.movis.domain.fee.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.fee.Fee;

@Getter
@NoArgsConstructor
public class FeeListElementDto {
    private String uuid;
    private String name;
    private Long amount;

    public FeeListElementDto(Fee fee) {
        uuid = fee.getUuid();
        this.name = fee.getName();
        this.amount =fee.getPaidAmount();
    }
}
