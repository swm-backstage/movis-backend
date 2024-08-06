package swm.backstage.movis.global.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EntityCntResDto {
    private Long count;

    public EntityCntResDto(Long count) {
        this.count = count;
    }
}
