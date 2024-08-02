package swm.backstage.movis.domain.fee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping()
    public void createFee(@RequestBody FeeReqDto feeReqDto){
        feeService.createFee(feeReqDto);
    }

    @PatchMapping()
    public void updateUnClassifiedFee(@RequestParam("feeId") String feeId ,@RequestBody FeeReqDto feeReqDto){
        feeService.updateUnClassifiedFee(feeId, feeReqDto);
    }

    @GetMapping()
    public FeeGetPagingListResDto getFeePagingList(@RequestParam String eventId,
                                                   @RequestParam LocalDateTime lastPaidAt,
                                                   @RequestParam(required = false, defaultValue = "first") String lastId,
                                                   @RequestParam(defaultValue = "20") int size){
        return feeService.getFeePagingList(eventId,lastPaidAt, lastId, size);
    }
}
