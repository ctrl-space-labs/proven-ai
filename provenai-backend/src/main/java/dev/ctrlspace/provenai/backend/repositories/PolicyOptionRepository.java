package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyOptionRepository extends JpaRepository<PolicyOption, UUID>, QuerydslPredicateExecutor<PolicyOption> {


    PolicyOption findByName(String policyName);

//    PolicyOption findByPolicyTypeId(UUID policyTypeId);

    List<PolicyOption> findByPolicyTypeId(UUID policyTypeId);


}
