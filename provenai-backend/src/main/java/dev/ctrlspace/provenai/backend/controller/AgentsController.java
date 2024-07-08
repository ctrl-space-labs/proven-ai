package dev.ctrlspace.provenai.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.controller.specs.AgentsControllerSpec;
import dev.ctrlspace.provenai.backend.converters.AgentConverter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.model.dtos.AgentDTO;
import dev.ctrlspace.provenai.backend.model.dtos.AgentIdCredential;
import dev.ctrlspace.provenai.backend.model.dtos.AgentPublicDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import dev.ctrlspace.provenai.backend.services.AgentService;
import id.walt.credentials.vc.vcs.W3CVC;
import jakarta.validation.Valid;
import org.json.JSONException;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@RestController
public class AgentsController implements AgentsControllerSpec {

    private AgentService agentService;

    private AgentConverter agentConverter;


    @Autowired
    public AgentsController(AgentService agentService,
                            AgentConverter agentConverter) {
        this.agentService = agentService;
        this.agentConverter = agentConverter;

    }


    // TODO: Authorization check that you can access only yours
    @GetMapping("/agents")
    public Page<Agent> getAllAgents(@Valid AgentCriteria criteria, Pageable pageable) throws ProvenAiException {

        return agentService.getAllAgents(criteria, pageable);
    }

    @GetMapping("/agents/public")
    public Page<AgentPublicDTO> getPublicAgentByCriteria(@Valid AgentCriteria criteria, Pageable pageable) throws ProvenAiException {

        return agentService.getPublicAgentByCriteria(criteria, pageable);
    }

    @GetMapping("/agents/{id}/policies")
    public Page<AgentPurposeOfUsePolicies> getAgentPurposeOfUsePolicies(@PathVariable UUID id, Pageable pageable) throws ProvenAiException {
        return agentService.getAgentPurposeOfUsePolicies(id, pageable);
    }



    @PostMapping("/agents")
    public Agent createAgent(@RequestBody AgentDTO agentDTO) throws ProvenAiException {
        Agent agent = agentConverter.toEntity(agentDTO);

        return agentService.createAgent(agent, agentDTO.getUsagePolicies());
    }


    @PostMapping("/agents/{id}/credential-offer")
    public AgentIdCredential createAgentVerifiableId(@PathVariable String id) throws ProvenAiException, JsonProcessingException, JSONException {
//        UserProfile agentProfile = (UserProfile) authentication.getPrincipal();
        AgentIdCredential agentIdCredential = new AgentIdCredential();
        W3CVC verifiableCredential = agentService.createAgentW3CVCByID(UUID.fromString(id));
        Object signedVcJwt = agentService.createAgentSignedVcJwt(verifiableCredential, UUID.fromString(id));
        agentIdCredential.setAgentId(id);
        agentIdCredential.setCredentialOfferUrl(agentService.createAgentVCOffer(verifiableCredential));
        agentIdCredential.setCredentialJwt(signedVcJwt);
        agentService.updateAgentVerifiableId(UUID.fromString(id), signedVcJwt.toString());
        return agentIdCredential;
    }


    @DeleteMapping("/agents/{id}")
    public void deleteAgent(@PathVariable UUID id) throws ProvenAiException {
        agentService.deleteAgent(id);
    }

    @PutMapping("/agents/{id}")
    public Agent updateAgent(@PathVariable UUID id, @RequestBody AgentDTO agentDTO) throws ProvenAiException {
        Agent agent = agentConverter.toEntity(agentDTO);
        agent.setId(id);
        return agentService.updateAgent(agent);
    }

    @GetMapping("/agents/{id}")
    public Agent getAgentById(@PathVariable UUID id) throws ProvenAiException {
        return agentService.getAgentById(id);
    }


    @PostMapping("/agents/token")
    public AccessTokenResponse authorizeAgent(@RequestParam("grant_type") String grantType,
                                              @RequestParam("scope") String scope,
                                              @RequestParam("vp_token") String vpToken) throws InterruptedException, ProvenAiException, ExecutionException, IOException {

        if (!"vp_token".equals(grantType)) {
            throw new ProvenAiException("INVALID_GRANT_TYPE", "Invalid grant type", HttpStatus.BAD_REQUEST);
        }

        Boolean verificationResult = agentService.verifyAgentVP(vpToken);
//          Boolean verificationResult = Boolean.TRUE;


//        verificationResult = Boolean.TRUE;
        if (verificationResult.equals(Boolean.TRUE)) {
            String agentVcJwt = agentService.getAgentVcJwt(vpToken);
            Agent agent = agentService.getAllAgents(AgentCriteria
                            .builder()
                            .agentVcJwt(agentVcJwt)
                            .build(),
                    Pageable.unpaged()).getContent().get(0);
            return agentService.getAgentAccessToken(agent.getAgentUsername(), scope);
        } else {
            throw new ProvenAiException("VP_VERIFICATION_FAILED", "VP Verification failed", HttpStatus.UNAUTHORIZED);
        }

    }
}



