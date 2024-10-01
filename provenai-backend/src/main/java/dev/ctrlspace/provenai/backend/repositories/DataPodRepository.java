package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.DataPod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface DataPodRepository extends JpaRepository<DataPod, UUID>, QuerydslPredicateExecutor<DataPod> {
    boolean existsByPodUniqueName(String podUniqueName);




    @Query(value = "SELECT dp.* \n" +
            "FROM proven_ai.data_pod dp \n" +
            "INNER JOIN (SELECT DISTINCT ap.data_pod_id \n" +
            "            FROM proven_ai.acl_policies ap \n" +
            "            LEFT JOIN (SELECT ap.data_pod_id \n" +
            "                        FROM proven_ai.acl_policies ap \n" +
            "                        WHERE ap.policy_type_id = (SELECT pt.id \n" +
            "                                                   FROM proven_ai.policy_types pt \n" +
            "                                                   WHERE pt.name = 'DENY_LIST') \n" +
            "                          AND ap.value = CAST(:agentId AS text)) AS subquery \n" +
            "            ON ap.data_pod_id = subquery.data_pod_id \n" +
            "            WHERE subquery.data_pod_id IS NULL) AS pods_not_denying_access \n" +
            "ON dp.id = pods_not_denying_access.data_pod_id \n" +
            "LEFT JOIN (SELECT DISTINCT pod_acl.data_pod_id \n" +
            "           FROM proven_ai.acl_policies pod_acl \n" +
            "           INNER JOIN proven_ai.agent_purpose_of_use_policies agent_acl \n" +
            "           ON (pod_acl.policy_type_id = agent_acl.policy_type_id \n" +
            "               AND pod_acl.policy_option_id = agent_acl.policy_option_id) \n" +
            "           WHERE agent_acl.agent_id = :agentId) AS match_usage_policies \n" +
            "ON dp.id = match_usage_policies.data_pod_id \n" +
            "LEFT JOIN (SELECT DISTINCT ap.data_pod_id \n" +
            "           FROM proven_ai.acl_policies ap \n" +
            "           WHERE ap.policy_type_id = (SELECT pt.id FROM proven_ai.policy_types pt WHERE pt.name = 'ALLOW_LIST') \n" +
            "             AND ap.value = CAST(:agentId AS text)) AS in_allow_list \n" +
            "ON dp.id = in_allow_list.data_pod_id \n" +
            "WHERE (match_usage_policies.data_pod_id IS NOT NULL \n" +
            "   OR in_allow_list.data_pod_id IS NOT NULL) and dp.is_disabled IS NOT TRUE",
            nativeQuery = true)
    List<DataPod> findAccessibleDataPodsForAgent(@Param("agentId") UUID agentId);
}






