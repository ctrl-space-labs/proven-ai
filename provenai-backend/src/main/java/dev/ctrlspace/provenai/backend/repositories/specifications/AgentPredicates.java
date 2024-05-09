package dev.ctrlspace.provenai.backend.repositories.specifications;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import dev.ctrlspace.provenai.backend.model.QAgent;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;

import java.util.UUID;

public class AgentPredicates {

    private static QAgent qAgent = QAgent.agent;

public static Predicate build(AgentCriteria criteria) {
        return ExpressionUtils.allOf(
                organizationId(criteria.getOrganizationId()),
                agentName(criteria.getAgentName())
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


}
