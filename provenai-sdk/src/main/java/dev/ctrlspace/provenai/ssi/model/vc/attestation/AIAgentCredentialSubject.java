package dev.ctrlspace.provenai.ssi.model.vc.attestation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AIAgentCredentialSubject {
    private String id;
    private String organizationName;
    private String agentName;
    private Instant creationDate;  // Assuming the use of java.util.Date; consider java.time.LocalDateTime for more precise control
    private String purpose;
    private List<Policy> usagePolicies;

}
