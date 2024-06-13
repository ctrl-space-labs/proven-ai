package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.AuditPermissionOfUseVc;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.dtos.AuditPermissionOfUseDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AuditPermissionOfUseVcCriteria;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.DataPodCriteria;
import dev.ctrlspace.provenai.backend.services.AuditPermissionOfUseVcService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
public class AuditPermissionOfUseVcController {


    private AuditPermissionOfUseVcService auditPermissionOfUseVcService;

    @Autowired
    public AuditPermissionOfUseVcController(AuditPermissionOfUseVcService auditPermissionOfUseVcService) {
        this.auditPermissionOfUseVcService = auditPermissionOfUseVcService;
    }


    @GetMapping("/audits-permission-of-use/{id}")
    public AuditPermissionOfUseVc getDataPodById(@PathVariable UUID id) throws ProvenAiException {
        return auditPermissionOfUseVcService.getAuditPermissionOfUseVcById(id);
    }

    @GetMapping("/permission-of-use-analytics")
    public List<AuditPermissionOfUseDTO> getAuditPermissionOfUseAnalytics(@Valid AuditPermissionOfUseVcCriteria criteria, Pageable pageable) throws ProvenAiException {


        return auditPermissionOfUseVcService.getAllAuditPermissionOfUseVc(criteria);
    }


}





