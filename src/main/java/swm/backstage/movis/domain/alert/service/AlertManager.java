package swm.backstage.movis.domain.alert.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.backstage.movis.domain.alert.dto.AlertGetRequestDTO;
import swm.backstage.movis.domain.alert.dto.BillType;
import swm.backstage.movis.domain.event_bill.EventBill;
import swm.backstage.movis.domain.event_bill.dto.EventBillCreateReqDto;
import swm.backstage.movis.domain.event_bill.service.EventBillService;
import swm.backstage.movis.domain.event_member.service.EventMemberService;
import swm.backstage.movis.domain.fee.dto.FeeReqDto;
import swm.backstage.movis.domain.fee.service.FeeService;

@Component
@RequiredArgsConstructor
public class AlertManager {

    private final FeeService feeService;
    private final EventBillService eventBillService;
    private final EventMemberService eventMemberService;

    public void alertConverter(AlertGetRequestDTO dto){
        switch (dto.getBillType()){
            case DEPOSIT:
                alertToFee(dto);
                break;
            case WITHDRAW:
                alertToEventBill(dto);
                break;
        }
    }

    public void alertToFee(AlertGetRequestDTO dto){
        String eventMemberId = eventMemberService.getEventMemberIdByAlertInfo(dto.getClubUid(), dto.getName(), dto.getCash());
        FeeReqDto newFeeReqDto = new FeeReqDto(
                dto.getClubUid(),
                eventMemberId,
                dto.getName(),
                dto.getCash(),
                dto.getCreatedAt()
        );
        feeService.createFeeByAlert(newFeeReqDto);
    }

    public void alertToEventBill(AlertGetRequestDTO dto){
        EventBillCreateReqDto newEventBillReqDto = new EventBillCreateReqDto(
                dto.getClubUid(),
                dto.getCash(),
                dto.getName(),
                dto.getCreatedAt()
        );
        eventBillService.createEventBill(newEventBillReqDto);
    }

}
