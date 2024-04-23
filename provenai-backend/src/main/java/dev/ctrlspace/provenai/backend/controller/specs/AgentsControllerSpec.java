package dev.ctrlspace.provenai.backend.controller.specs;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiErrorResponse;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.dtos.AgentAuthorizationRequestDTO;
import dev.ctrlspace.provenai.backend.model.dtos.AgentIdCredential;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Agents", description = "Endpoints for managing agents")
public interface AgentsControllerSpec {

    @Operation(summary = "Get all agents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved agents"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Page<Agent> getAgents(AgentCriteria criteria, Authentication authentication);


    @Operation(summary = "Create a new agent",
            description = "Creates a new agent with the usage policies provided in the portal.</br>" +
                    "Once the agent has been created in the backend, a verifiable id can be issued for the agent.</br>" +
                    "the 'credential-offer' endpoint can be used to create a verifiable id for the agent.")
    public Agent createAgents(Agent agent);

    @Operation(summary = "Create a verifiable id for an agent",
            description = "Creates a verifiable id for the agent with the id provided in the request.</br>" +
                    "The verifiable id is issued by the ProvenAI issuer portal and can be used to authenticate the agent in the ProvenAI ecosystem.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a verifiable id. </br> " +
                    "The response contain the actual VC JWT and the credential offer URL to be used to load the VC into user's wallet.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AgentIdCredential.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProvenAiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public AgentIdCredential createAgentVerifiableId(@PathVariable String id);


    @Operation(summary = "Authorization request for an agent",
            description = "Verifies the agent id presentation and returns an authorization token.</br>" +
                    "Agents need to provide a Verifiable Agent ID Presentation, once the presentation is verified, " +
                    "an authorization token is provided to the agent.</br>" +
                    "The authorization token can be used to authenticate the agent in the ProvenAI ecosystem.")
    public String authorizeAgent(@RequestBody AgentAuthorizationRequestDTO agentIdVP, @PathVariable String id);
}
