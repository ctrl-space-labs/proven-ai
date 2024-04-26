package dev.ctrlspace.provenai.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.controller.specs.AgentsControllerSpec;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.dtos.AgentAuthorizationRequestDTO;
import dev.ctrlspace.provenai.backend.model.dtos.AgentIdCredential;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import dev.ctrlspace.provenai.ssi.issuer.ProveAIIssuer;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
//import kotlinx.serialization.json.internal.JsonException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
public class AgentsController implements AgentsControllerSpec {


    @GetMapping("/agents")
    public Page<Agent> getAgents(AgentCriteria criteria, Authentication authentication ) {
//        VerifiableCredential<NaturalPersonCredentialSubject> vc = new VerifiableCredential<>();
        return Page.empty();
    }

    @PostMapping("/agents")
    public Agent createAgents(Agent agent) {
        return new Agent();
    }

    @PostMapping("/agents/{id}/credential-offer")
    public AgentIdCredential createAgentVerifiableId(@PathVariable String id) throws Exception, JsonProcessingException {
//        VerifiableCredential<AIAgentCredentialSubject> agentIdVC = VerifiableCredential.<AIAgentCredentialSubject>builder()
//                .type("VerifiableAIAgent")
//                .validFrom(new Date())
//                .credentialSubject(AIAgentCredentialSubject.builder()
//                        .id("agentId")
//                        .organizationName("Ctrl+Space Labs")
//                        .agentName("John Doe")
//                        .creationDate(new Date().toInstant())
//                        .purpose("AI Agent")
//                        .build())
//                .build();
//
//        ProveAIIssuer provenAISDK = new ProveAIIssuer();
//        String agentIdVCJwt = provenAISDK.generateUnsignedVC(agentIdVC);

        //String url = ProvenAIIssueService.getOfferFor(agentIdVCJwt)
        //String signedjwt = provenAISDK.generateSignedVC(agentIdVCJwt)




//
//        return AgentIdCredential
//                .builder()
//                .agentId(id)
//                .credentialOfferUrl(url)
//                .credentialJwt(signedjwt)
//                .build();
        return null;
    }


    /**
     * Verifies the agent id presentation and returns an authorization token
     *
     * @param agentIdVP
     * @param id
     * @return
     */
    @PostMapping("/agents/{id}/token")
    public String authorizeAgent(@RequestBody AgentAuthorizationRequestDTO agentIdVP, @PathVariable String id) {
        return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsIm5hbWUiOiJKb2huIERvZSIsImVtYWlsIjoiam9obkBkb2UuY29tIn0.7J1Gzv";
    }






}