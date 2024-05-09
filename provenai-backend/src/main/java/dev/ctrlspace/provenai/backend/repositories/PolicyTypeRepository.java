package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface PolicyTypeRepository extends JpaRepository<PolicyType, UUID>, QuerydslPredicateExecutor<PolicyType> {
}
