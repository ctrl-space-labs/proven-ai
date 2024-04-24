package dev.ctrlspace.provenai.backend.controller;

import dev.ctrlspace.provenai.backend.controller.specs.AgentsControllerSpec;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.dtos.AgentAuthorizationRequestDTO;
import dev.ctrlspace.provenai.backend.model.dtos.AgentIdCredential;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


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
    public AgentIdCredential createAgentVerifiableId(@PathVariable String id) {
        return AgentIdCredential
                .builder()
                .agentId(id)
                .credentialOfferUrl("openid-credential-offer://issuer.portal.walt.id/?credential_offer_uri=https%3A%2F%2Fissuer.portal.walt.id%2Fopenid4vc%2FcredentialOffer%3Fid%3D99d79908-32e6-4bd7-9873-562d2d262e12")
                .credentialJwt("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsIm5hbWUiOiJKb2huIERvZSIsImVtYWlsIjoiam9obkBkb2UuY29tIn0.7J1Gzv")
                .build();
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