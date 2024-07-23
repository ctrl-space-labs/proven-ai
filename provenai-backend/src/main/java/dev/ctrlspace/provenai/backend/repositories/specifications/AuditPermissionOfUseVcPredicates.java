package dev.ctrlspace.provenai.backend.repositories.specifications;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import dev.ctrlspace.provenai.backend.model.QAuditPermissionOfUseVc;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AuditPermissionOfUseVcCriteria;
import jakarta.persistence.Query;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class AuditPermissionOfUseVcPredicates {

    private static QAuditPermissionOfUseVc qAuditPermissionOfUseVc = QAuditPermissionOfUseVc.auditPermissionOfUseVc;

    public static Predicate build(AuditPermissionOfUseVcCriteria criteria) {
        return ExpressionUtils.allOf(
                searchId(criteria.getSearchId()),
                documentIscc(criteria.getDocumentIscc()),
                ownerOrganizationId(criteria.getOwnerOrganizationId()),
                processorOrganizationId(criteria.getProcessorOrganizationId()),
                processorOrganizationDid(criteria.getProcessorOrganizationDid()),
                ownerDataPodId(criteria.getOwnerDataPodIdIn()),
                processorAgentId(criteria.getProcessorAgentIdIn()),
                embeddingModel(criteria.getEmbeddingModel()),
                createdFrom(criteria.getFrom()),
                createdTo(criteria.getTo())
        );
    }

    public static String nativeQueryBuild(AuditPermissionOfUseVcCriteria criteria) {
        StringBuilder whereClause = new StringBuilder();

        if (criteria.getOwnerOrganizationId() != null) {
            whereClause.append("apu.owner_organization_id = :ownerOrganizationId AND \n");
        }
        if (criteria.getProcessorOrganizationId() != null) {
            whereClause.append("apu.processor_organization_id = :processorOrganizationId AND \n");
        }
        if (criteria.getSearchId() != null) {
            whereClause.append("apu.search_id = :searchId AND \n");
        }
        if (criteria.getDocumentIscc() != null) {
            whereClause.append("apu.document_iscc = :documentIscc AND \n");
        }
        if (criteria.getOwnerDataPodIdIn() != null && !criteria.getOwnerDataPodIdIn().isEmpty()) {
            whereClause.append("apu.owner_datapod_id IN :ownerDataPodIdIn AND \n");
        }
        if (criteria.getProcessorAgentIdIn() != null && !criteria.getProcessorAgentIdIn().isEmpty()) {
            whereClause.append("apu.processor_agent_id IN :processorAgentIdIn AND \n");
        }
        if (criteria.getEmbeddingModel() != null) {
            whereClause.append("apu.embedding_model = :embeddingModel AND \n");
        }
        if (criteria.getFrom() != null) {
            whereClause.append("apu.created_at >= :from AND \n");
        }
        if (criteria.getTo() != null) {
            whereClause.append("apu.created_at <= :to AND \n");
        }

        // Remove trailing "AND \n" if present
        if (whereClause.length() > 0) {
            whereClause.setLength(whereClause.length() - 5);
        }

        return whereClause.toString();
    }


    public static void setNativeQueryParameters(Query query, AuditPermissionOfUseVcCriteria criteria) {
        if (criteria.getOwnerOrganizationId() != null) {
            query.setParameter("ownerOrganizationId", UUID.fromString(criteria.getOwnerOrganizationId()));
        }
        if (criteria.getProcessorOrganizationId() != null) {
            query.setParameter("processorOrganizationId", UUID.fromString(criteria.getProcessorOrganizationId()));
        }
        if (criteria.getSearchId() != null) {
            query.setParameter("searchId", UUID.fromString(criteria.getSearchId()));
        }
        if (criteria.getDocumentIscc() != null) {
            query.setParameter("documentIscc", criteria.getDocumentIscc());
        }
        if (criteria.getOwnerDataPodIdIn() != null && !criteria.getOwnerDataPodIdIn().isEmpty()) {
            query.setParameter("ownerDataPodIdIn", criteria.getOwnerDataPodIdIn().stream().map(UUID::fromString).collect(Collectors.toList()));
        }
        if (criteria.getProcessorAgentIdIn() != null && !criteria.getProcessorAgentIdIn().isEmpty()) {
            query.setParameter("processorAgentIdIn", criteria.getProcessorAgentIdIn().stream().map(UUID::fromString).collect(Collectors.toList()));
        }
        if (criteria.getEmbeddingModel() != null) {
            query.setParameter("embeddingModel", criteria.getEmbeddingModel());
        }
        if (criteria.getFrom() != null) {
            query.setParameter("from", Timestamp.from(criteria.getFrom()));
        }
        if (criteria.getTo() != null) {
            query.setParameter("to", Timestamp.from(criteria.getTo()));
        }
    }

    private static Predicate processorAgentId(List<String> processorAgentIdIn) {
        if (processorAgentIdIn == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.processorAgentId
                .in(processorAgentIdIn.stream().map(UUID::fromString).toArray(UUID[]::new));
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

    private static Predicate processorOrganizationDid(String processorOrganizationDid) {
        if (processorOrganizationDid == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.processorOrganizationDid.eq(processorOrganizationDid);
    }

    private static Predicate ownerDataPodId(List<String> datapodId) {
        if (datapodId == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.ownerDataPodId
                .in(datapodId.stream().map(UUID::fromString).toArray(UUID[]::new));
    }

    private static Predicate embeddingModel(String embeddingModel) {
        if (embeddingModel == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.embeddingModel.eq(embeddingModel);
    }

    private static Predicate createdFrom(Instant from) {
        if (from == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.createdAt.goe(from);
    }

    private static Predicate createdTo(Instant to) {
        if (to == null) {
            return null;
        }
        return qAuditPermissionOfUseVc.createdAt.loe(to);
    }
}
