package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PolicyTypeRepository extends JpaRepository<PolicyType, UUID>, QuerydslPredicateExecutor<PolicyType> {

    PolicyType findByName(String policyName);



}
