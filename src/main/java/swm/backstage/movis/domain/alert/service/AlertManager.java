package swm.backstage.movis.domain.alert.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.backstage.movis.domain.alert.dto.AlertGetRequestDTO;
import swm.backstage.movis.domain.alert.dto.BillType;
import swm.backstage.movis.domain.event_member.service.EventMemberService;
import swm.backstage.movis.domain.fee.dto.FeeReqDto;
import swm.backstage.movis.domain.fee.service.FeeService;

@Component
@RequiredArgsConstructor
public class AlertManager {

    private final FeeService feeService;
    private final EventMemberService eventMemberService;

    public void alertToFee(AlertGetRequestDTO dto){
        String eventMemberId = eventMemberService.getEventMemberIdByAlertInfo(dto.getCludUid(), dto.getName(), dto.getCash());
        FeeReqDto newFeeReqDto = new FeeReqDto(
                dto.getCludUid(),
                eventMemberId,
                dto.getName(),
                dto.getCash() * (dto.getBillType() == BillType.WITHDRAW ? -1 : 1),
                dto.getCreatedAt()
        );

        feeService.createFee(newFeeReqDto);
    }
}
