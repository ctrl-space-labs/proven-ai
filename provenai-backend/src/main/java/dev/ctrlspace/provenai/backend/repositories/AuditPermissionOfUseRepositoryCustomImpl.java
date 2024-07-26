package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.QAuditPermissionOfUseVc;
import dev.ctrlspace.provenai.backend.model.dtos.AuditPermissionOfUseDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AuditPermissionOfUseVcCriteria;
import dev.ctrlspace.provenai.backend.repositories.specifications.AuditPermissionOfUseVcPredicates;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuditPermissionOfUseRepositoryCustomImpl implements AuditPermissionOfUseRepositoryCustom {


    private static QAuditPermissionOfUseVc qAuditPermissionOfUseVc = QAuditPermissionOfUseVc.auditPermissionOfUseVc;


    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Return a list of the sum of tokens per time period, per ownerDataPodId, ownerOrganizationId, processorAgentId, processorOrganizationId, and time interval
     * @param criteria
     * @return
     */
    @Override
    public List<AuditPermissionOfUseDTO> findAuditPermissionsAnalytics(AuditPermissionOfUseVcCriteria criteria) {
        StringBuilder sql = new StringBuilder("SELECT \n" +
                "apu.owner_datapod_id as ownerDatapodId, \n" +
                "apu.owner_organization_id as ownerOrganizationId, \n" +
                "apu.processor_agent_id as processorAgentId, \n" +
                "apu.processor_organization_id as processorOrganizationId, \n" +
                "to_timestamp(FLOOR(EXTRACT(EPOCH FROM apu.created_at) / :intervalInSeconds) * :intervalInSeconds) AS bucketStart, \n" +
                "SUM(apu.tokens) AS sumTokens \n" +
                "FROM proven_ai.audit_permission_of_use_vc apu \n");

        // Build dynamic WHERE clause
        String whereClause = AuditPermissionOfUseVcPredicates.nativeQueryBuild(criteria);
        if (!whereClause.isEmpty()) {
            sql.append("WHERE ").append(whereClause).append(" \n");
        }

        sql.append("GROUP BY apu.owner_datapod_id, apu.owner_organization_id, apu.processor_agent_id, apu.processor_organization_id, bucketStart \n");
        sql.append("ORDER BY bucketStart");

        Query query = entityManager.createNativeQuery(sql.toString(), "AuditPermissionOfUseDTOMapping");
        query.setParameter("intervalInSeconds", criteria.getTimeIntervalInSeconds());

        AuditPermissionOfUseVcPredicates.setNativeQueryParameters(query, criteria);

        return query.getResultList();

    }
}
