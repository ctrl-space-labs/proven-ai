package dev.ctrlspace.provenai.backend.repositories.specifications;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.JPAExpressions;
import dev.ctrlspace.provenai.backend.model.QAclPolicies;
import dev.ctrlspace.provenai.backend.model.QDataPod;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.DataPodCriteria;

import java.util.UUID;

public class DataPodPredicates {

    private static QDataPod qDataPod = QDataPod.dataPod;
    private static QAclPolicies qAclPolicies = QAclPolicies.aclPolicies;

    public static Predicate build(DataPodCriteria criteria) {
        Predicate predicate = ExpressionUtils.allOf(organizationId(criteria.getOrganizationId()),
//                denyAgentId(criteria.getDenyAgentId())),
                allowAgentId(criteria.getAllowAgentId()));

        // If predicate is null, return a default Predicate that always evaluates to true
        return predicate != null ? predicate : Expressions.asBoolean(true).isTrue();


    }

    private static Predicate organizationId(String organizationId) {
        if (StringUtils.isNullOrEmpty(organizationId)) {
            return null;
        }
        return qDataPod.organizationId.eq(UUID.fromString(organizationId));
    }

    private static Predicate allowAgentId(String allowAgentId) {
        if (StringUtils.isNullOrEmpty(allowAgentId)) {
            return null;
        }

        // Create a subquery for aclPolicies
        QAclPolicies aclPoliciesSubQuery = new QAclPolicies("aclPoliciesSubQuery");

        // Apply the conditions to the subquery
        BooleanExpression subQueryExpression = aclPoliciesSubQuery.value.eq(allowAgentId).and(aclPoliciesSubQuery.policyType.name.eq("ALLOW_LIST"));

        // Use the subquery in the main query
        // TODO - Performance Test for more than 1 million Agents and Pods
        return qDataPod.aclPolicies.any()
                .in(JPAExpressions.selectFrom(aclPoliciesSubQuery).where(subQueryExpression));
    }

//    private static Predicate denyAgentId(String denyAgentId) {
//        if (StringUtils.isNullOrEmpty(denyAgentId)) {
//            return null;
//        }
//        return qDataPod.denyAgentId.eq(UUID.fromString(denyAgentId));
//    }
}
