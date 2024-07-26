package dev.ctrlspace.provenai.backend.repositories.specifications;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import dev.ctrlspace.provenai.backend.model.QAclPolicies;
import dev.ctrlspace.provenai.backend.model.QAgent;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;

import java.util.List;
import java.util.UUID;

public class AgentPredicates {

    private static QAgent qAgent = QAgent.agent;

    public static Predicate build(AgentCriteria criteria) {

        // If fetchAll is true, return a predicate that always evaluates to true
        if (criteria.isFetchAll()) {
            return qAgent.isNotNull();
        }

        return ExpressionUtils.allOf(
                organizationId(criteria.getOrganizationId()),
                agentName(criteria.getAgentName()),
                agentVcJwt(criteria.getAgentVcJwt()),
                agentIdIn(criteria.getAgentIdIn())

        );
    }

    private static Predicate organizationId(String organizationId) {
        if (organizationId == null) {
            return null;
        }
        return qAgent.organizationId.eq(UUID.fromString(organizationId));
    }

    private static Predicate agentName(String agentName) {
        if (agentName == null) {
            return null;
        }
        return qAgent.agentName.eq(agentName);
    }

    private static Predicate agentVcJwt(String agentVcJwt) {
        if (agentVcJwt == null) {
            return null;
        }
        return qAgent.agentVcJwt.eq(agentVcJwt);
    }

    private static Predicate agentIdIn(List<UUID> agentIdIn) {
        if (agentIdIn == null || agentIdIn.isEmpty()) {
            return null;
        }
        return qAgent.id.in(agentIdIn);
    }

}



