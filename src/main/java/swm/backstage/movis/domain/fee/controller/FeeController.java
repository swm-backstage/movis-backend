package swm.backstage.movis.domain.fee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.fee.Fee;
import swm.backstage.movis.domain.fee.dto.FeeDto;
import swm.backstage.movis.domain.fee.dto.FeeGetPagingListResDto;
import swm.backstage.movis.domain.fee.service.FeeService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fees")
@Slf4j
public class FeeController {
    private final FeeService feeService;

    @PostMapping()
    public void createFee(@RequestBody FeeDto feeDto){
        feeService.createFee(feeDto);
    }

    @PatchMapping()
    public void updateUnClassifiedFee(@RequestBody FeeDto feeDto ){
        feeService.updateUnClassifiedFee(feeDto);
    }

    @GetMapping()
    public FeeGetPagingListResDto getFeePagingList(@RequestParam String eventId,
                                                   @RequestParam(required = false, defaultValue = "first") String lastId,
                                                   @RequestParam(defaultValue = "20") int size){
        return feeService.getFeePagingList(eventId, lastId, size);
    }
}
