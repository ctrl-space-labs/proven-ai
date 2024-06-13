package dev.ctrlspace.provenai.backend.repositories.specifications;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import dev.ctrlspace.provenai.backend.model.QAclPolicies;
import dev.ctrlspace.provenai.backend.model.QAgent;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.Policy;

import java.util.UUID;

public class AgentPredicates {

    private static QAgent qAgent = QAgent.agent;

    public static Predicate build(AgentCriteria criteria) {
        if (criteria == null || areAllCriteriaFieldsNull(criteria)) {
            return qAgent.isNotNull(); // Return all agents if criteria is null or all fields are null
        }

        return ExpressionUtils.allOf(
                organizationId(criteria.getOrganizationId()),
                agentName(criteria.getAgentName()),
                agentVcJwt(criteria.getAgentVcJwt())


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

    private static boolean areAllCriteriaFieldsNull(AgentCriteria criteria) {
        return criteria.getOrganizationId() == null &&
                criteria.getAgentName() == null &&
                criteria.getAgentPurposeOfUsePolicies() == null &&
                criteria.getPolicyIn() == null &&
                criteria.getPolicy() == null;
    }


    private static Predicate agentVcJwt(String agentVcJwt) {
        if (agentVcJwt == null) {
            return null;
        }
        return qAgent.agentVcJwt.eq(agentVcJwt);
    }

}



