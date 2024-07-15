package swm.backstage.movis.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.member.dto.MemberCreateListDto;
import swm.backstage.movis.domain.member.dto.MemberGetListDto;
import swm.backstage.movis.domain.member.service.MemberService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping()
    public void createMemberList(@RequestBody @Validated MemberCreateListDto memberCreateListDto) {
        memberService.createAll(memberCreateListDto);
    }

    @GetMapping()
    public MemberGetListDto getMemberList(@RequestParam(name = "clubId") String clubId) {
        return new MemberGetListDto(memberService.getMemberList(clubId));
    }


}
