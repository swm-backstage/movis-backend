package swm.backstage.movis.domain.event_bill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.backstage.movis.domain.club.service.ClubService;
import swm.backstage.movis.domain.event_bill.EventBill;
import swm.backstage.movis.domain.event_bill.dto.EventBillCreateDto;
import swm.backstage.movis.domain.event_bill.repository.EventBillRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventBillService {
    private final EventBillRepository eventBillRepository;
    private final ClubService clubService;

    public void createEventBill(EventBillCreateDto eventBillCreateDto){
        eventBillRepository.save(new EventBill(UUID.randomUUID().toString(),eventBillCreateDto,clubService.getClubByUuId(eventBillCreateDto.getClubId())));
    }
}
