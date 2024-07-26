package dev.ctrlspace.provenai.backend.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "policy_options", schema = "proven_ai")
public class PolicyOption {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private UUID id;

    @Basic
    @Column(name = "policy_type_id")
    private UUID policyTypeId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getPolicyTypeId() {return policyTypeId;}

    public void setPolicyTypeId(UUID policyTypeId) {this.policyTypeId = policyTypeId;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolicyOption that = (PolicyOption) o;
        return Objects.equals(id, that.id) && Objects.equals(policyTypeId, that.policyTypeId) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, policyTypeId, name, description);
    }
}
