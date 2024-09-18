package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.controller.specs.AuditPermissionOfUseVcControllerSpec;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.AuditPermissionOfUseVc;
import dev.ctrlspace.provenai.backend.model.dtos.AuditPermissionOfUseDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AuditPermissionOfUseVcCriteria;
import dev.ctrlspace.provenai.backend.services.AuditPermissionOfUseVcService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
public class AuditPermissionOfUseVcController implements AuditPermissionOfUseVcControllerSpec {


    private AuditPermissionOfUseVcService auditPermissionOfUseVcService;

    @Autowired
    public AuditPermissionOfUseVcController(AuditPermissionOfUseVcService auditPermissionOfUseVcService) {
        this.auditPermissionOfUseVcService = auditPermissionOfUseVcService;
    }


//    @GetMapping("/audits-permission-of-use/{id}")
//    public AuditPermissionOfUseVc getDataPodById(@PathVariable UUID id) throws ProvenAiException {
//        return auditPermissionOfUseVcService.getAuditPermissionOfUseVcById(id);
//    }

    @GetMapping("/permission-of-use-analytics")
    public List<AuditPermissionOfUseDTO> getAuditPermissionOfUseAnalytics(@Valid AuditPermissionOfUseVcCriteria criteria, Pageable pageable) throws ProvenAiException {
        return auditPermissionOfUseVcService.getAuditPermissionOfUseAnalytics(criteria);
    }

    @PostMapping("/permission-of-use-analytics/audit/blockchain-registration")
    public void auditPermissionOfUseBlockchainRegistration(@RequestParam String organizationDid, @RequestParam Instant date) throws Exception {
        auditPermissionOfUseVcService.generateAndRegisterDailyUsageToBlockchain(organizationDid, date);
        int x = 5;
    }

    @PostMapping("/permission-of-use-analytics/audit/verification")
    public void auditPermissionOfUseVerification(@RequestParam String organizationDid, @RequestParam Instant date, AuditPermissionOfUseVcCriteria criteria) throws Exception {
        auditPermissionOfUseVcService.generateAndVerifyProofs(organizationDid, date, criteria);
        int x = 5;
    }



}





