package swm.backstage.movis.domain.fee.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.fee.Fee;

@Getter
@NoArgsConstructor
public class FeeGetPagingElementResDto {
    private String ulid;
    private String name;
    private Long amount;

    public FeeGetPagingElementResDto(Fee fee) {
        this.ulid = fee.getUlid();
        this.name = fee.getName();
        this.amount =fee.getPaidAmount();
    }
}
