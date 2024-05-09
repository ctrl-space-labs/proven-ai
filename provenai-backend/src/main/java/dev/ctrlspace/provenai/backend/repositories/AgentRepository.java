package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

public interface AgentRepository extends JpaRepository<Agent, UUID>, QuerydslPredicateExecutor<Agent> {

    @Transactional
    @Modifying
    @Query("update Agent a set a.agentVcId = :agentVcId where a.id = :id")
    void updateAgentVerifiableId(@Param(value = "id") UUID id, @Param(value = "agentVcId") String agentVcId);

}
