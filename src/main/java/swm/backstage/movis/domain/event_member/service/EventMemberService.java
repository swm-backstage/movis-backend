package swm.backstage.movis.domain.event_member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event_member.dto.EventMemberListDto;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.event_member.repository.EventMemberJdbcRepository;
import swm.backstage.movis.domain.event_member.repository.EventMemberJpaRepository;
import swm.backstage.movis.domain.member.service.MemberService;
import swm.backstage.movis.global.error.ErrorCode;
import swm.backstage.movis.global.error.exception.BaseException;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventMemberService {

    private final EventMemberJpaRepository eventMemberJpaRepository;
    private final EventMemberJdbcRepository eventMemberJdbcRepository;
    private final EventService eventService;
    private final MemberService memberService;

    @Transactional
    public EventMember getEventMemberByUuid(String eventMemberId) {
        return eventMemberJpaRepository.findByUuid(eventMemberId)
                .orElseThrow(()->new BaseException("account book is not found", ErrorCode.ELEMENT_NOT_FOUND));
    }

    @Transactional
    public void addEventMembers(EventMemberListDto eventMemberListDto) {
        Event event = eventService.getEventByUuid(eventMemberListDto.getEventId());

        Set<Long> memberSet = event.getEventMembers().stream()
                .map(eventMember -> eventMember.getMember().getId())
                .collect(Collectors.toSet());


        eventMemberJdbcRepository.bulkSave(
                memberService.getMemberListByUuids(eventMemberListDto.getIdList()).stream()
                        .filter(member -> !memberSet.contains(member.getId()))
                        .map(member->new EventMember(UUID.randomUUID().toString(),member,event))
                        .toList()
        );
    }

    public String getEventMemberIdByAlertInfo(String clubId, String name, Long amount){

        //1. clubId,  amount로 event 찾기 -> 못찾으면 null 반환
        Event event = eventService.findEventByClubIdAndAmount(clubId, amount);
        if(event == null){
            return null;
        }
        //2. event + name으로 eventMemberId 찾기 -> 못찾으면 null 반환
        EventMember eventMember = eventMemberJpaRepository.findByEventAndMemberName(event, name).orElse(null);
        if(eventMember == null){
            return null;
        }
        return eventMember.getUuid();
    }


}
