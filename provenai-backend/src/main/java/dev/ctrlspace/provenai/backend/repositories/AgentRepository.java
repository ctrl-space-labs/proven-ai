package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID>, QuerydslPredicateExecutor<Agent> {

    boolean existsByAgentUsername(String agentUsername);

    @Transactional
    @Modifying
    @Query("update Agent a set a.agentVcJwt = :agentVcJwt where a.id = :id")
    void updateAgentVerifiableId(@Param(value = "id") UUID id, @Param(value = "agentVcJwt") String agentVcId);

    Agent findByAgentName(String agentName);

    Optional<Agent> findByAgentUsername(String agentUsername);

    public boolean existsByIdAndOrganizationIdIn(UUID id, List<String> organizationId);


    @Query(value = "SELECT DISTINCT policy.value AS data_pod_id " +
            "FROM proven_ai.agent_purpose_of_use_policies policy " +
            "INNER JOIN proven_ai.policy_types pt ON policy.policy_type_id = pt.id " +
            "INNER JOIN proven_ai.policy_options po ON policy.policy_option_id = po.id " +
            "WHERE policy.agent_id = :agentId " +
            "AND pt.name = 'ALLOW_LIST'",
            nativeQuery = true)
    List<UUID> findAllowListDataPodIdsForAgent(@Param("agentId") UUID agentId);


    @Query(value = "SELECT DISTINCT policy.value AS data_pod_id " +
            "FROM proven_ai.agent_purpose_of_use_policies policy " +
            "INNER JOIN proven_ai.policy_types pt ON policy.policy_type_id = pt.id " +
            "INNER JOIN proven_ai.policy_options po ON policy.policy_option_id = po.id " +
            "WHERE policy.agent_id = :agentId " +
            "AND pt.name = 'DENY_LIST'",
            nativeQuery = true)
    List<UUID> findDenyListDataPodIdsForAgent(@Param("agentId") UUID agentId);


}