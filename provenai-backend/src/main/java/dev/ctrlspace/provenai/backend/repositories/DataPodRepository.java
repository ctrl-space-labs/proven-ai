package dev.ctrlspace.provenai.backend.repositories;

import dev.ctrlspace.provenai.backend.model.DataPod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DataPodRepository extends JpaRepository<DataPod, UUID>, QuerydslPredicateExecutor<DataPod> {
    boolean existsByPodUniqueName(String podUniqueName);
}
