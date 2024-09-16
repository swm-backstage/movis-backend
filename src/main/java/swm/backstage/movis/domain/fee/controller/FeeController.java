package swm.backstage.movis.domain.fee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event_bill.dto.EventBIllCreateExplanationReqDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillGetResDto;
import swm.backstage.movis.domain.fee.dto.*;
import swm.backstage.movis.domain.fee.service.FeeService;

import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fees")
@Slf4j
public class FeeController {
    private final FeeService feeService;
    /**
     * 회비 수동 입력
     * */
    @PreAuthorize("hasPermission(#eventId, 'eventId', {'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @PostMapping("/input")
    public void createFeeByInput(@RequestParam("eventId") @Param("eventId") String eventId,
                                 @RequestBody FeeInputReqDto feeInputReqDto){
        feeService.createFeeByInput(eventId,feeInputReqDto);
    }
    /**
     * 회비 생성 (알림용)
     * */
    @PreAuthorize("hasPermission(#feeReqDto.clubId, 'clubId', {'ROLE_MANAGER'})")
    @PostMapping()
    public void createFeeByAlert(@RequestBody @Param("feeReqDto") FeeReqDto feeReqDto){
        feeService.createFeeByAlert(feeReqDto);
    }
    /**
     * 회비 수동 분류
     * */
    @PreAuthorize("hasPermission(#feeReqDto.clubId, 'clubId', {'ROLE_MANAGER'})")
    @PatchMapping()
    public void updateUnClassifiedFee(@RequestParam("feeId") String feeId ,@RequestBody @Param("feeReaDto") FeeReqDto feeReqDto){
        feeService.updateUnClassifiedFee(feeId, feeReqDto);
    }
    /**
     * 회비 페이징 조회 (무한스크롤)
     * */
    @PreAuthorize("hasPermission(#eventId, 'eventId', {'ROLE_MEMBER', 'ROLE_EXECUTIVE', 'ROLE_MANAGER'})")
    @GetMapping()
    public FeeGetPagingListResDto getFeePagingList(@RequestParam @Param("eventId") String eventId,
                                                   @RequestParam LocalDateTime lastPaidAt,
                                                   @RequestParam(required = false, defaultValue = "first") String lastId,
                                                   @RequestParam(defaultValue = "20") int size){
        return feeService.getFeePagingList(eventId,lastPaidAt, lastId, size);
    }
    /**
     * 회비 단일 조회
     * */
    //TODO: 권한 검증
    @GetMapping("/{feeId}")
    public FeeGetResDto getFee(@PathVariable String feeId) {
        return new FeeGetResDto(feeService.getFeeByUuId(feeId));
    }
    /**
     * 회비 설명 추가
     * */
    //TODO: 권한 검증
    @PostMapping("/explanation")
    public FeeGetResDto createEventBillExplanation(@RequestBody FeeCreateExplanationReqDto reqDto){
        return new FeeGetResDto(feeService.createFeeExplanation(reqDto));
    }

}
