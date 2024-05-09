package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface PolicyOptionRepository extends JpaRepository<PolicyOption, UUID>, QuerydslPredicateExecutor<PolicyOption> {


    PolicyOption findByName(String policyName);

    }
