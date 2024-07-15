package swm.backstage.movis.domain.event_member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_member.dto.EventMemberListDto;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.event_member.repository.EventMemberRepository;
import swm.backstage.movis.domain.member.service.MemberService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventMemberService {

    private final EventMemberRepository eventMemberRepository;
    private final EventService eventService;
    private final MemberService memberService;

    @Transactional
    public EventMember getEventMemberByUuid(String eventMemberId) {
        return eventMemberRepository.findByUuid(eventMemberId)
                .orElseThrow(()->new BaseException("account book is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    @Transactional
    public void addEventMembers(EventMemberListDto eventMemberListDto) {
        Event event = eventService.getEventByUuid(eventMemberListDto.getEventId());

        Set<Long> memberSet = event.getEventMembers().stream()
                .map(eventMember -> eventMember.getMember().getId())
                .collect(Collectors.toSet());


        event.getEventMembers().addAll(
                memberService.getMemberListByUuids(eventMemberListDto.getIdList()).stream()
                        .filter(member -> !memberSet.contains(member.getId()))
                        .map(member->new EventMember(UUID.randomUUID().toString(),member,event))
                        .toList()
        );
    }


}
