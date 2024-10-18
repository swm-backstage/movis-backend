package swm.backstage.movis.multi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import swm.backstage.movis.domain.auth.dto.request.UserCreateReqDto;
import swm.backstage.movis.domain.auth.service.AuthService;
import swm.backstage.movis.domain.club.Club;
import swm.backstage.movis.domain.club.dto.ClubCreateReqDto;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.event.Event;
import swm.backstage.movis.domain.event.dto.EventCreateReqDto;
import swm.backstage.movis.domain.event.service.EventManager;
import swm.backstage.movis.domain.event.service.EventService;
import swm.backstage.movis.domain.event_bill.dto.EventBillInputReqDto;
import swm.backstage.movis.domain.event_bill.service.EventBillService;
import swm.backstage.movis.domain.event_member.EventMember;
import swm.backstage.movis.domain.event_member.dto.EventMemberListReqDto;
import swm.backstage.movis.domain.event_member.service.EventMemberService;
import swm.backstage.movis.domain.fee.dto.FeeInputReqDto;
import swm.backstage.movis.domain.fee.service.FeeService;
import swm.backstage.movis.domain.member.Member;
import swm.backstage.movis.domain.member.dto.MemberCreateListDto;
import swm.backstage.movis.domain.member.dto.MemberCreateReqDto;
import swm.backstage.movis.domain.member.service.MemberService;
import swm.backstage.movis.global.error.exception.BaseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
@Disabled
public class EventTest {

    private static final Logger log = LoggerFactory.getLogger(EventTest.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ClubService clubService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FeeService feeService;

    @Autowired
    private EventBillService eventBillService;

    @Autowired
    private EventMemberService eventMemberService;

    @Autowired
    private EventManager eventManager;

    private String clubId;
    private String eventId;

    @BeforeEach
    void init(){
        //user 생성
        UserCreateReqDto userCreateReqDto = new UserCreateReqDto("rlfehd12","Ghdrlfehd112@","홍길동","010-1234-5678");
        authService.register(userCreateReqDto);

        // club 생성
        ClubCreateReqDto reqDto = new ClubCreateReqDto("소마","15기 화이팅","7948","093","image");
        Club club = clubService.createClub(reqDto,"rlfehd12");
        clubId = club.getUlid();

        //Event 생성
        EventCreateReqDto eventCreateReqDto = new EventCreateReqDto(club.getUlid(),"이벤트1",null,List.of());
        Event event = eventService.createEvent(eventCreateReqDto);
        eventId = event.getUlid();

        //member 등록
        MemberCreateReqDto memberCreateReqDto1 = new MemberCreateReqDto("안희찬","01011112222");
        MemberCreateListDto memberCreateListDto = new MemberCreateListDto(club.getUlid(), List.of(memberCreateReqDto1));
        memberService.createAll(memberCreateListDto);

        //eventMember 생성
        List<String> memberIdList = memberService.getMemberList(club.getUlid())
                .stream()
                .map(Member::getUlid)
                .toList();
        eventMemberService.addEventMembers(new EventMemberListReqDto(event.getUlid(),memberIdList));
        EventMember eventMember = eventMemberService.getEventMemberList(event.getUlid()).get(0);


        //Fee 생성
        for(int i =0 ;i<5;i++){
            Long amount = i * 1000L + 1000L;
            FeeInputReqDto feeInputReqDto = new FeeInputReqDto(eventMember.getUlid(),amount, LocalDateTime.now(),"입금내역","설명");
            feeService.createFeeByInput(event.getUlid(),feeInputReqDto);
        }

        //EventBill 생성
        for(int i =0 ;i<5;i++){
            Long amount = i * 1000L + 1000L;
            EventBillInputReqDto eventBillInputReqDto = new EventBillInputReqDto(-amount, LocalDateTime.now(),"입금내역","설명","이미지");
            eventBillService.createEventBillByInput(event.getUlid(),eventBillInputReqDto);
        }

    }

    @Test
    void deleteMethodMultiThreadTest(){

        int threadCount = 5;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for(int i = 0; i<threadCount; i++){
            int number = i;
            executorService.submit(()->{
                try{
                    countDownLatch.await();
                    log.info("Thread start");
                    eventManager.deleteEvent(clubId,eventId);
                }
                catch(BaseException e){
                    log.info(e.getErrorCode().toString());
                    Thread.currentThread().interrupt();
                }
                catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            });
        }

        countDownLatch.countDown();
        executorService.shutdown();
    }
}
//identifier : rlfehd12