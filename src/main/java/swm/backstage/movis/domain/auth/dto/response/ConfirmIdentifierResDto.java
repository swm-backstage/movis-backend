package swm.backstage.movis.domain.auth.dto.response;


import lombok.Getter;

@Getter
public class ConfirmIdentifierResDto {

    private boolean duplicated;

    public ConfirmIdentifierResDto(boolean duplicated) {
        this.duplicated = duplicated;
    }
}
