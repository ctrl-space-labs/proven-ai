package dev.ctrlspace.provenai.backend.repositories.specifications;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import dev.ctrlspace.provenai.backend.model.QAgent;
import dev.ctrlspace.provenai.backend.model.QAuditPermissionOfUseVc;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AuditPermissionOfUseVcCriteria;

import java.util.UUID;


public class AuditPermissionOfUseVcPredicates {

    private static QAuditPermissionOfUseVc qAuditPermissionOfUseVc = QAuditPermissionOfUseVc.auditPermissionOfUseVc;

    public static Predicate build(AuditPermissionOfUseVcCriteria criteria) {
        return ExpressionUtils.allOf(
                searchId(criteria.getSearchId()),
                documentIscc(criteria.getDocumentIscc()),
                ownerOrganizationId(criteria.getOwnerOrganizationId()),
                processorOrganizationId(criteria.getProcessorOrganizationId()),
                datapodId(criteria.getDatapodId()),
                embeddingModel(criteria.getEmbeddingModel())
        );
    }

    private static Predicate searchId(String searchId) {
        if (searchId == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.searchId.eq(UUID.fromString(searchId));
    }

    private static Predicate documentIscc(String documentIscc) {
        if (documentIscc == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.documentIscc.eq(documentIscc);
    }

    private static Predicate ownerOrganizationId(String ownerOrganizationId) {
        if (ownerOrganizationId == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.ownerOrganizationId.eq(UUID.fromString(ownerOrganizationId));
    }

    private static Predicate processorOrganizationId(String processorOrganizationId) {
        if (processorOrganizationId == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.processorOrganizationId.eq(UUID.fromString(processorOrganizationId));
    }

    private static Predicate datapodId(String datapodId) {
        if (datapodId == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.dataPodId.eq(UUID.fromString(datapodId));
    }

    private static Predicate embeddingModel(String embeddingModel) {
        if (embeddingModel == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.embeddingModel.eq(embeddingModel);
    }
}
