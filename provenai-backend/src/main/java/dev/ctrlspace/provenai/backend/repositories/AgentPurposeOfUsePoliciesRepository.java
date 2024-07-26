package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentPurposeOfUsePoliciesRepository extends JpaRepository<AgentPurposeOfUsePolicies, UUID>, QuerydslPredicateExecutor<AgentPurposeOfUsePolicies> {

    List<AgentPurposeOfUsePolicies> findByAgentId(UUID agentId);

    Page<AgentPurposeOfUsePolicies> findByAgentId(UUID agentId, Pageable pageable);


}




