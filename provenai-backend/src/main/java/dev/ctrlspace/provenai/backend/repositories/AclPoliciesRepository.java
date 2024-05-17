package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.AclPolicies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AclPoliciesRepository extends JpaRepository<AclPolicies, UUID>, QuerydslPredicateExecutor<AclPolicies> {


    List<AclPolicies> findByDataPodId(UUID dataPodId);


}
