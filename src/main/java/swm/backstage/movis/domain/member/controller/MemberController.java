package swm.backstage.movis.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.member.dto.MemberCreateListDto;
import swm.backstage.movis.domain.member.dto.MemberGetListResDto;
import swm.backstage.movis.domain.member.service.MemberService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PreAuthorize("hasPermission(#memberCreateListDto.clubId, 'clubId', {'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @PostMapping()
    public void createMemberList(@RequestBody @Validated @Param("memberCreateListDto") MemberCreateListDto memberCreateListDto) {
        memberService.createAll(memberCreateListDto);
    }

    @PreAuthorize("hasPermission(#clubId, 'clubId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping()
    public MemberGetListResDto getMemberList(@RequestParam(name = "clubId") @Param("clubId") String clubId) {
        return new MemberGetListResDto(memberService.getMemberList(clubId));
    }


}
