package swm.backstage.movis.domain.auth.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckIdentifierResDto {

    private boolean exists;
}
