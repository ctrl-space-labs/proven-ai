package dev.ctrlspace.provenai.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.controller.specs.AgentsControllerSpec;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.dtos.AgentAuthorizationRequestDTO;
import dev.ctrlspace.provenai.backend.model.dtos.AgentIdCredential;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
//import kotlinx.serialization.json.internal.JsonException;
import dev.ctrlspace.provenai.backend.services.AgentService;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
import id.walt.credentials.vc.vcs.W3CVC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
public class AgentsController implements AgentsControllerSpec {

   private AgentService agentService;

   @Autowired
    public AgentsController(AgentService agentService) {
       this.agentService = agentService;

   }


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
       AgentIdCredential agentIdCredential = new AgentIdCredential();
        W3CVC verifiableCredential = agentService.createAgentVerifiableCredentialID(UUID.fromString(id));
        agentIdCredential.setAgentId(String.valueOf(verifiableCredential));
//        agentIdCredential.setCredentialJwt();
//        agentIdCredential.setCredentialOfferUrl();


        return agentIdCredential;
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