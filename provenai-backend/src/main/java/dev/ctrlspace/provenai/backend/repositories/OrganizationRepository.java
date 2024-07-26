package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationRepository  extends JpaRepository<Organization, UUID>, QuerydslPredicateExecutor<Organization> {
}
