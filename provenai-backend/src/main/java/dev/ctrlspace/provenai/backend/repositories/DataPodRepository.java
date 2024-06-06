package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.DataPod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DataPodRepository extends JpaRepository<DataPod, UUID>, QuerydslPredicateExecutor<DataPod> {
    boolean existsByPodUniqueName(String podUniqueName);




    @Query(value = "select * \n" +
            "from proven_ai.data_pod dp \n" +
            "         inner join (SELECT DISTINCT ap.data_pod_id \n" +
            "                     FROM proven_ai.acl_policies ap \n" +
            "                              LEFT JOIN (SELECT ap.data_pod_id \n" +
            "                                         FROM proven_ai.acl_policies ap \n" +
            "                                         WHERE ap.policy_type_id = (SELECT pt.id \n" +
            "                                                                    FROM proven_ai.policy_types pt \n" +
            "                                                                    WHERE name = 'DENY_LIST') \n" +
            "                                           AND ap.value = :agent_id) AS subquery \n" +
            "                                        ON ap.id = subquery.data_pod_id \n" +
            "                     WHERE subquery.data_pod_id IS NULL) as pods_not_denying_access \n" +
            "                    on dp.id = pods_not_denying_access.data_pod_id \n" +
            "         left join (select distinct data_pod_id \n" +
            "                    from proven_ai.acl_policies pod_acl \n" +
            "                             inner join proven_ai.agent_purpose_of_use_policies agent_acl \n" +
            "                                        on (pod_acl.policy_type_id = agent_acl.policy_type_id \n" +
            "                                            and pod_acl.policy_option_id = agent_acl.policy_option_id) \n" +
            "                    where agent_acl.agent_id = :agent_id) as match_usage_policies \n" +
            "                   on dp.id = match_usage_policies.data_pod_id \n" +
            "         left join (select distinct data_pod_id \n" +
            "                    from proven_ai.acl_policies ap \n" +
            "                    where ap.policy_type_id = (select pt.id from proven_ai.policy_types pt where name = 'ALLOW_LIST') \n" +
            "                      and ap.value = :agent_id) as in_allow_list \n" +
            "                   on dp.id = in_allow_list.data_pod_id \n" +
            "where match_usage_policies.data_pod_id is not null \n" +
            "   OR in_allow_list.data_pod_id is not null",
            nativeQuery = true)
    List<DataPod> findAccessibleDataPodsForAgent(@Param("agentId") UUID agentId);


}
