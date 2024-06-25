package dev.ctrlspace.provenai.backend.controller.specs;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiErrorResponse;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Agent;
import dev.ctrlspace.provenai.backend.model.dtos.AgentDTO;
import dev.ctrlspace.provenai.backend.model.dtos.AgentIdCredential;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AgentCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONException;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Tag(name = "Agents", description = "Endpoints for managing AI Agents. Full CRUD operations are supported.</br>" +
        "AI Agents are entities that can be used to perform search request in the ProvenAI. Each Agent must be uniquely identified " +
        "using Verifiable Agent ID. The Verifiable Agent ID is issued by the ProvenAI issuer portal and can be used to authenticate the agent in the ProvenAI ecosystem." +
        "Agents already registered can use the /token endpoint to authenticate themselves and get an authorization token.")
public interface AgentsControllerSpec {

    @Operation(summary = "Get all agents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved agents"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    Page<Agent> getAllAgents(
            @Parameter(description = "Filtering criteria for Agents", required = false, schema = @Schema(implementation = AgentCriteria.class))
            AgentCriteria criteria,
            @Parameter(description = "Pagination information", required = false, schema = @Schema(implementation = Pageable.class))
            Pageable pageable) throws ProvenAiException;


    @Operation(summary = "Get agent by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved agent by ID"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    public Agent getAgentById(@PathVariable UUID id) throws ProvenAiException;


    @Operation(summary = "Create a new agent",
            description = "Creates a new agent with the usage policies provided in the portal.</br>" +
                    "Once the agent has been created in the backend, a verifiable id can be issued for the agent.</br>" +
                    "the 'credential-offer' endpoint can be used to create a verifiable id for the agent.")
    public Agent createAgent(@RequestBody AgentDTO agentDTO) throws ProvenAiException;

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
    public AgentIdCredential createAgentVerifiableId(@PathVariable String id) throws ProvenAiException, JsonProcessingException, JSONException;


    @Operation(summary = "Update an agent",
            description = "Updates an agent with the id provided in the request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the agent",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Agent.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProvenAiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Agent updateAgent(@PathVariable UUID id, @RequestBody AgentDTO agentDTO) throws ProvenAiException;


    @Operation(summary = "Delete an agent",
            description = "Deletes an agent with the id provided in the request.</br>" +
                    "Once the agent has been deleted, the usage policies associated with the agent will be deleted.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the agent"),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProvenAiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void deleteAgent(@PathVariable UUID id) throws ProvenAiException;


    @Operation(summary = "Authorization request for an agent",
            description = "Verifies the agent id presentation and returns an authorization token.</br>" +
                    "Agents need to provide a Verifiable Agent ID Presentation, once the presentation is verified, " +
                    "an authorization token is provided to the agent.</br>" +
                    "The authorization token can be used to authenticate the agent in the ProvenAI ecosystem.</br></br>" +
                    "This uses the " +
                    "<a href='https://hub.ebsi.eu/conformance/learn/verifiable-presentation-exchange#service-to-service-token-flow'>EBSI Service to Service Token Flow</a>" +
                    " to authorize the agent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authorized the agent. </br> " +
                    "The response contain the authorization token.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProvenAiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public AccessTokenResponse authorizeAgent(@Parameter(description = "Grant type to be used. Must be 'vp_token'", example = "vp_token", required = true)
                                 @RequestParam("grant_type") String grantType,
                                              @Parameter(description = "Scope of the request.", example = "openid", required = true)
                                 @RequestParam("scope") String scope,
                                              @Parameter(description = "The Verifiable Agent ID Presentation Token in JWT format", required = true)
                                 @RequestParam("vp_token") String vpToken) throws InterruptedException, ProvenAiException, ExecutionException, IOException;


}




