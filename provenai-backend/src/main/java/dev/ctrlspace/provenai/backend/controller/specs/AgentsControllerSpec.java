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
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            @ApiResponse(responseCode = "200", description = "Successfully created a verifiable id. The response contains the actual VC JWT and the credential offer URL to be used to load the VC into user's wallet.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AgentIdCredential.class),
                            examples = @ExampleObject(value = "{\"agentId\": \"123e4567-e89b-12d3-a456-426614174000\", \"credentialOfferUrl\": \"openid-credential-offer://issuer.portal.walt.id/?credential_offer_uri=https%3A%2F%2Fissuer.portal.walt.id%2Fopenid4vc%2FcredentialOffer%3Fid%3Dc85078d0-61e5-462b-abe5-a460584d7343\"," +
                                    " \"credentialJwt\": \"eyJhbGciOiJFZERTQSIsImtpZCI6ImRpZDpqd2s6ZXlKcmRIa2lPaUpQUzFBaUxDSmpjbllpT2lKRlpESTFOVEU1SWl3aWEybGtJam9pUTBaUkxVNXlZVFY1Ym5sQ2MyWjRkM2szWVU1bU9HUjFRVVZWUTAxc1RVbHlVa2x5UkdjMlJFbDVOQ0lzSW5naU9pSm9OVzVpZHpaWU9VcHRTVEJDZG5WUk5VMHdTbGhtZWs4NGN6SmxSV0pRWkZZeU9YZHpTRlJNT1hCckluMCJ9.eyJpc3MiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaVEwWlJMVTV5WVRWNWJubENjMlo0ZDNrM1lVNW1PR1IxUVVWVlEwMXNUVWx5VWtseVJHYzJSRWw1TkNJc0luZ2lPaUpvTlc1aWR6WllPVXB0U1RCQ2RuVlJOVTB3U2xobWVrODRjekpsUldKUVpGWXlPWGR6U0ZSTU9YQnJJbjAiLCJzdWIiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaVEwWlJMVTV5WVRWNWJubENjMlo0ZDNrM1lVNW1PR1IxUVVWVlEwMXNUVWx5VWtseVJHYzJSRWw1TkNJc0luZ2lPaUpvTlc1aWR6WllPVXB0U1RCQ2RuVlJOVTB3U2xobWVrODRjekpsUldKUVpGWXlPWGR6U0ZSTU9YQnJJbjAiLCJ2YyI6eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSIsImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL3YxIl0sInR5cGUiOlsiVmVyaWZpYWJsZUNyZWRlbnRpYWwiLCJWZXJpZmlhYmxlQUlBZ2VudCJdLCJjcmVkZW50aWFsU3ViamVjdCI6eyJhZ2VudCI6eyJpZCI6ImRpZDpqd2s6ZXlKcmRIa2lPaUpQUzFBaUxDSmpjbllpT2lKRlpESTFOVEU1SWl3aWEybGtJam9pUTBaUkxVNXlZVFY1Ym5sQ2MyWjRkM2szWVU1bU9HUjFRVVZWUTAxc1RVbHlVa2x5UkdjMlJFbDVOQ0lzSW5naU9pSm9OVzVpZHpaWU9VcHRTVEJDZG5WUk5VMHdTbGhtZWs4NGN6SmxSV0pRWkZZeU9YZHpTRlJNT1hCckluMCIsIm9yZ2FuaXphdGlvbk5hbWUiOiJFeGFtcGxlIE9yZ2FuaXphdGlvbiIsImFnZW50TmFtZSI6bnVsbCwiY3JlYXRpb25EYXRlIjoiMjAyNC0wNS0wOVQxNDo0MzoyNi4zNzIxNzU1MDBaIiwidXNhZ2VQb2xpY2llcyI6W3sicG9saWN5VHlwZSI6IkNPTVBFTlNBVElPTl9QT0xJQ1kiLCJwb2xpY3lWYWx1ZSI6IkZJWEVEIn0seyJwb2xpY3lUeXBlIjoiVVNBR0VfUE9MSUNZIiwicG9saWN5VmFsdWUiOiJHRU5FUkFMX0FTU0lTVEFOVCJ9LHsicG9saWN5VHlwZSI6IkNPTVBFTlNBVElPTl9QT0xJQ1kiLCJwb2xpY3lWYWx1ZSI6IkZJWEVEIn0seyJwb2xpY3lUeXBlIjoiVVNBR0VfUE9MSUNZIiwicG9saWN5VmFsdWUiOiJHRU5FUkFMX0FTU0lTVEFOVCJ9XX0sImlkIjoiZGlkOmp3azpleUpyZEhraU9pSlBTMUFpTENKamNuWWlPaUpGWkRJMU5URTVJaXdpYTJsa0lqb2lRMFpSTFU1eVlUVjVibmxDYzJaNGQzazNZVTVtT0dSMVFVVlZRMDFzVFVseVVrbHlSR2MyUkVsNU5DSXNJbmdpT2lKb05XNWlkelpZT1VwdFNUQkNkblZSTlUwd1NsaG1lazg0Y3pKbFJXSlFaRll5T1hkelNGUk1PWEJySW4wIn0sImlkIjoidXJuOnV1aWQ6bnVsbCIsImlzc3VlciI6IlwiZGlkOmp3azpleUpyZEhraU9pSlBTMUFpTENKamNuWWlPaUpGWkRJMU5URTVJaXdpYTJsa0lqb2lRMFpSTFU1eVlUVjVibmxDYzJaNGQzazNZVTVtT0dSMVFVVlZRMDFzVFVseVVrbHlSR2MyUkVsNU5DSXNJbmdpT2lKb05XNWlkelpZT1VwdFNUQkNkblZSTlUwd1NsaG1lazg0Y3pKbFJXSlFaRll5T1hkelNGUk1PWEJySW4wXCIiLCJpc3N1YW5jZURhdGUiOiIyMDI0LTA1LTA5VDE0OjQzOjI2LjU4MzUyMTUwMFoiLCJleHBpcmF0aW9uRGF0ZSI6IjIwMjQtMDYtMDhUMTQ6NDM6MjYuNjM1WiJ9fQ.XWCmjQgLIJBWPeW2ih6sV__cVRAxxkp84_HH7AVrJOBav3gwCvdnzogd3yZ7xVKMavLrhwYHlKTON9TrdcdUCQ\"}"))),
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




