package swm.backstage.movis.domain.alert.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.backstage.movis.domain.alert.dto.AlertGetRequestDTO;
import swm.backstage.movis.domain.alert.service.AlertManager;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertManager alertManager;

    @PreAuthorize("hasPermission(#dto.clubUid, 'clubId', {'ROLE_MANAGER'})")
    @PostMapping
    public void createFee(@RequestBody @Param("dto") AlertGetRequestDTO dto) {
        alertManager.alertConverter(dto);
    }
}
