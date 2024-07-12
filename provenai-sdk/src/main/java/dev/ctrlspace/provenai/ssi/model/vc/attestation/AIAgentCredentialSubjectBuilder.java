package dev.ctrlspace.provenai.ssi.model.vc.attestation;

import dev.ctrlspace.provenai.ssi.model.vc.CredentialSubject;

import java.time.Instant;
import java.util.List;

public class AIAgentCredentialSubjectBuilder implements CredentialSubject {
    private String id;
    private String organizationName;
    private String agentName;
    private String agentId;
    private Instant creationDate;
    private List<Policy> usagePolicies;

    public AIAgentCredentialSubjectBuilder id(String id) {
        this.id = id;
        return this;
    }

    public AIAgentCredentialSubjectBuilder organizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    public AIAgentCredentialSubjectBuilder agentName(String agentName) {
        this.agentName = agentName;
        return this;
    }

    public AIAgentCredentialSubjectBuilder agentId(String agentId) {
        this.agentId = agentId;
        return this;
    }

    public AIAgentCredentialSubjectBuilder creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public AIAgentCredentialSubjectBuilder usagePolicies(List<Policy> usagePolicies) {
        this.usagePolicies = usagePolicies;
        return this;
    }

    public AIAgentCredentialSubject build() {
        return new AIAgentCredentialSubject(id, organizationName, agentName, agentId, creationDate, usagePolicies);
    }

    @Override
    public String getId() {
        return null;
    }
}
