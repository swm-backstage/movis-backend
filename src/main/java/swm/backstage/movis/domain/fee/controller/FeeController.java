package swm.backstage.movis.domain.fee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import swm.backstage.movis.domain.fee.dto.FeeDto;
import swm.backstage.movis.domain.fee.service.FeeService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fees")
public class FeeController {
    private final FeeService feeService;

    @PostMapping()
    private void createFee(@RequestBody FeeDto feeDto){
        feeService.createFee(feeDto);
    }

    @PatchMapping()
    private void updateUnClassifiedFee(@RequestBody FeeDto feeDto ){
        feeService.updateUnClassifiedFee(feeDto);
    }

}
