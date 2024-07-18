package dev.ctrlspace.provenai.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.authentication.ProvenAIAuthenticationToken;
import dev.ctrlspace.provenai.backend.controller.specs.AgentsControllerSpec;
import dev.ctrlspace.provenai.backend.converters.AgentConverter;
import dev.ctrlspace.provenai.backend.converters.AgentPurposeOfUsePoliciesConverter;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.AgentPurposeOfUsePolicies;
import dev.ctrlspace.provenai.backend.model.authentication.OrganizationUserDTO;
import dev.ctrlspace.provenai.backend.model.authentication.UserProfile;
import dev.ctrlspace.provenai.backend.model.dtos.*;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AccessCriteria;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import dev.ctrlspace.provenai.backend.services.AgentPurposeOfUsePoliciesService;
import dev.ctrlspace.provenai.backend.services.AgentService;
import dev.ctrlspace.provenai.backend.utils.SecurityUtils;
import dev.ctrlspace.provenai.ssi.verifier.ProvenAIVerifier;
import id.walt.credentials.vc.vcs.W3CVC;
import jakarta.validation.Valid;
import org.json.JSONException;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@RestController
public class AgentsController implements AgentsControllerSpec {

    private AgentService agentService;

    private AgentConverter agentConverter;

    private AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService;
    private AgentPurposeOfUsePoliciesConverter agentPurposeOfUsePoliciesConverter;

    private SecurityUtils securityUtils;


    @Autowired
    public AgentsController(AgentService agentService,
                            AgentConverter agentConverter,
                            AgentPurposeOfUsePoliciesService agentPurposeOfUsePoliciesService,
                            AgentPurposeOfUsePoliciesConverter agentPurposeOfUsePoliciesConverter,
                            SecurityUtils securityUtils) {
        this.agentService = agentService;
        this.agentConverter = agentConverter;
        this.agentPurposeOfUsePoliciesService = agentPurposeOfUsePoliciesService;
        this.agentPurposeOfUsePoliciesConverter = agentPurposeOfUsePoliciesConverter;
        this.securityUtils = securityUtils;

    }


    @PreAuthorize("@securityUtils.hasAuthority('OP_READ_PROVEN_AI_AGENT', 'getRequestedAgentsFromRequestParams')")
    @GetMapping("/agents")
    public Page<Agent> getAllAgents(@Valid AgentCriteria criteria, Pageable pageable, Authentication authentication) throws ProvenAiException {

        if (securityUtils.isUser() &&
                criteria.getAgentIdIn().isEmpty()) {
            UserProfile userProfile = (UserProfile) authentication.getPrincipal();

            List<UUID> authorizedAgentIds = userProfile
                    .getOrganizations()
                    .stream()
                    .filter(org -> org.getAuthorities().contains("OP_READ_PROVEN_AI_AGENT"))
                    .flatMap(org -> org.getProjectAgents().stream())
                    .map(agent -> UUID.fromString(String.valueOf(agent.getId())))
                    .collect(Collectors.toList());

            criteria.setAgentIdIn(authorizedAgentIds);

        }


        return agentService.getAllAgents(criteria, pageable);

    }

    @GetMapping("/agents/public")
    public Page<AgentPublicDTO> getPublicAgentByCriteria(@Valid AgentCriteria criteria, Pageable pageable, Authentication authentication) throws ProvenAiException {

        return agentService.getPublicAgentByCriteria(criteria, pageable);
    }

    @PreAuthorize("@securityUtils.hasAuthority('OP_READ_PROVEN_AI_AGENT', 'getRequestedAgentIdFromPathVariable')")
    @GetMapping("/agents/{agentId}")
    public Agent getAgentById(@PathVariable UUID agentId) throws ProvenAiException {
        return agentService.getAgentById(agentId);
    }

    @PreAuthorize("@securityUtils.hasAuthority('OP_READ_PROVEN_AI_AGENT', 'getRequestedAgentIdFromPathVariable')")
    @GetMapping("/agents/{agentId}/policies")
    public Page<AgentPurposeOfUsePolicies> getAgentPurposeOfUsePolicies(@PathVariable UUID agentId, Pageable pageable) throws ProvenAiException {
        return agentService.getAgentPurposeOfUsePolicies(agentId, pageable);
    }


    @PostMapping("/agents")
    public Agent createAgent(@RequestBody AgentDTO agentDTO) throws ProvenAiException {
        Agent agent = agentConverter.toEntity(agentDTO);

        return agentService.createAgent(agent, agentDTO.getUsagePolicies());
    }


    @PreAuthorize("@securityUtils.hasAuthority('OP_OFFER_PROVEN_AI_AGENT_VC', 'getRequestedAgentIdFromPathVariable')")
    @PostMapping("/agents/{agentId}/credential-offer")
    public AgentIdCredential createAgentVerifiableId(@PathVariable String agentId) throws ProvenAiException, JsonProcessingException, JSONException {
//        UserProfile agentProfile = (UserProfile) authentication.getPrincipal();
        AgentIdCredential agentIdCredential = new AgentIdCredential();
        W3CVC verifiableCredential = agentService.createAgentW3CVCByID(UUID.fromString(agentId));
        Object signedVcJwt = agentService.createAgentSignedVcJwt(verifiableCredential, UUID.fromString(agentId));
        agentIdCredential.setAgentId(agentId);
        agentIdCredential.setCredentialOfferUrl(agentService.createAgentVCOffer(verifiableCredential));
        agentIdCredential.setCredentialJwt(signedVcJwt);
        agentService.updateAgentVerifiableId(UUID.fromString(agentId), signedVcJwt.toString());
        return agentIdCredential;
    }

    @PreAuthorize("@securityUtils.hasAuthority('OP_DELETE_PROVEN_AI_AGENT', 'getRequestedAgentIdFromPathVariable')")
    @DeleteMapping("/agents/{agentId}")
    public void deleteAgent(@PathVariable UUID agentId) throws ProvenAiException {
        agentService.deleteAgent(agentId);
    }

    @PreAuthorize("@securityUtils.hasAuthority('OP_EDIT_PROVEN_AI_AGENT', 'getRequestedAgentIdFromPathVariable')")
    @PutMapping("/agents/{agentId}")
    public Agent updateAgent(@PathVariable UUID agentId, @RequestBody AgentDTO agentDTO) throws ProvenAiException {
        Agent agent = agentConverter.toEntity(agentDTO);
        agent.setId(agentId);
        return agentService.updateAgent(agent);
    }


    @PreAuthorize("@securityUtils.hasAuthority('OP_EDIT_PROVEN_AI_AGENT', 'getRequestedAgentIdFromPathVariable')")
    @PostMapping(value = "/agents/{agentId}/agent-purpose-of-use-policies", consumes = {"application/json"})
    public AgentPurposeOfUsePolicies createAgentPurposeOfUsePolicy(@PathVariable UUID agentId, @RequestBody AgentPurposeOfUsePoliciesDTO agentPurposeOfUsePoliciesDTO) throws ProvenAiException {
        AgentPurposeOfUsePolicies agentPurposeOfUsePolicies = agentPurposeOfUsePoliciesConverter.toEntity(agentPurposeOfUsePoliciesDTO);

        if (!agentId.equals(agentPurposeOfUsePoliciesDTO.getAgentId())) {
            throw new ProvenAiException("AGENT_ID_MISMATCH", "ID in path and ID in body are not the same", HttpStatus.BAD_REQUEST);
        }
        return agentPurposeOfUsePoliciesService.createAgentPurposeOfUsePolicy(agentPurposeOfUsePolicies);
    }

    @PreAuthorize("@securityUtils.hasAuthority('OP_EDIT_PROVEN_AI_AGENT', 'getRequestedAgentIdFromPathVariable')")
    @DeleteMapping("/agents/{agentId}/agent-purpose-of-use-policies")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAgentPurposeOfUsePolicies(@RequestParam List<UUID> agentPurposeOfUsePolicyIds) throws ProvenAiException {
        agentPurposeOfUsePoliciesService.deleteAgentPurposeOfUsePolicies(agentPurposeOfUsePolicyIds);
    }


    @PostMapping("/agents/token")
    public AccessTokenResponse authorizeAgent(@RequestParam("grant_type") String grantType,
                                              @RequestParam("scope") String scope,
                                              @RequestParam("vp_token") String vpToken) throws InterruptedException, ProvenAiException, ExecutionException, IOException {

        if (!"vp_token".equals(grantType)) {
            throw new ProvenAiException("INVALID_GRANT_TYPE", "Invalid grant type", HttpStatus.BAD_REQUEST);
        }
        ProvenAIVerifier provenAIVerifier = new ProvenAIVerifier();

        Boolean verificationResult = provenAIVerifier.verifyVPJwt(vpToken);
//          Boolean verificationResult = Boolean.TRUE;


        verificationResult = Boolean.TRUE;
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



