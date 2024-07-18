package swm.backstage.movis.domain.fee.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.fee.Fee;

import java.util.List;

@Getter
@NoArgsConstructor
public class FeeGetPagingListResDto {
    List<FeeListElementDto> feeElements;
    private Boolean isLast;

    public FeeGetPagingListResDto(List<Fee> feeList, Boolean isLast) {
        this.isLast = isLast;
        this.feeElements = feeList.stream()
                .map(FeeListElementDto::new)
                .toList();
    }
}
