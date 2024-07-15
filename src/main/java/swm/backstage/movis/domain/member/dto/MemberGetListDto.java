package swm.backstage.movis.domain.member.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.backstage.movis.domain.member.Member;

import java.util.List;

@Getter
@NoArgsConstructor
public class MemberGetListDto {
    private List<MemberGetDto> members;

    public MemberGetListDto(List<Member> members) {
        this.members = members.stream().map(MemberGetDto::new).toList();
    }
}
