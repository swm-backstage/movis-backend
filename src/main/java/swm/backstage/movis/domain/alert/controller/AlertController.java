package swm.backstage.movis.domain.alert.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.backstage.movis.domain.alert.dto.AlertGetRequestDTO;
import swm.backstage.movis.domain.alert.service.AlertManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertManager alertManager;

    @PostMapping
    public void createFee(AlertGetRequestDTO dto) {
        alertManager.alertToFee(dto);
    }
}
