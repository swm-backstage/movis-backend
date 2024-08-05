package swm.backstage.movis.domain.fee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.event_bill.dto.EventBIllCreateExplanationReqDto;
import swm.backstage.movis.domain.event_bill.dto.EventBillGetResDto;
import swm.backstage.movis.domain.fee.dto.FeeCreateExplanationReqDto;
import swm.backstage.movis.domain.fee.dto.FeeGetResDto;
import swm.backstage.movis.domain.fee.dto.FeeReqDto;
import swm.backstage.movis.domain.fee.dto.FeeGetPagingListResDto;
import swm.backstage.movis.domain.fee.service.FeeService;

import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fees")
@Slf4j
public class FeeController {
    private final FeeService feeService;
    /**
     * 회비 생성
     * */
    @PostMapping()
    public void createFee(@RequestBody FeeReqDto feeReqDto){
        feeService.createFee(feeReqDto);
    }
    /**
     * 회비 수동 분류
     * */
    @PatchMapping()
    public void updateUnClassifiedFee(@RequestParam("feeId") String feeId ,@RequestBody FeeReqDto feeReqDto){
        feeService.updateUnClassifiedFee(feeId, feeReqDto);
    }
    /**
     * 회비 페이징 조회 (무한스크롤)
     * */
    @GetMapping()
    public FeeGetPagingListResDto getFeePagingList(@RequestParam String eventId,
                                                   @RequestParam LocalDateTime lastPaidAt,
                                                   @RequestParam(required = false, defaultValue = "first") String lastId,
                                                   @RequestParam(defaultValue = "20") int size){
        return feeService.getFeePagingList(eventId,lastPaidAt, lastId, size);
    }
    /**
     * 회비 단일 조회
     * */
    @GetMapping("/{feeId}}")
    public FeeGetResDto getFee(@PathVariable String feeId) {
        return new FeeGetResDto(feeService.getFeeByUuId(feeId));
    }
    /**
     * 회비 설명 추가
     * */
    @PostMapping("/explanation")
    public FeeGetResDto createEventBillExplanation(@RequestBody FeeCreateExplanationReqDto reqDto){
        return new FeeGetResDto(feeService.createFeeExplanation(reqDto));
    }

}
