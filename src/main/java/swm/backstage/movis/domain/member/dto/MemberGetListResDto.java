package swm.backstage.movis.domain.member.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.member.Member;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemberGetListResDto {
    private List<MemberGetElementResDto> members;

    public MemberGetListResDto(List<Member> members) {
        this.members = members.stream().map(MemberGetElementResDto::new).toList();
    }
}
