package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.dtos.AuditPermissionOfUseDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AuditPermissionOfUseVcCriteria;

import java.util.List;

public interface AuditPermissionOfUseRepositoryCustom {

    List<AuditPermissionOfUseDTO> findAuditPermissionsAnalytics(AuditPermissionOfUseVcCriteria criteria);

}
