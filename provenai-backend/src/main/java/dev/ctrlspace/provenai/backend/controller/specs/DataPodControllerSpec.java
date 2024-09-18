package dev.ctrlspace.provenai.backend.controller.specs;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.AclPolicies;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.dtos.AclPoliciesDTO;
import dev.ctrlspace.provenai.backend.model.dtos.DataPodDTO;
import dev.ctrlspace.provenai.backend.model.dtos.DataPodPublicDTO;
import dev.ctrlspace.provenai.backend.model.dtos.VCOfferDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.DataPodCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Data Pods",
        description = "Endpoints for managing data pods. Full CRUD operations are supported.</br>" +
                "Data Pods can be created by organizations to store metadata of the actual data like that location in an external system and an Access Control List (ACL) specifying the Policies under which the data can be accessed. " +
                "Data in a specific Pods can be shared with AI Agents only if the Agent's usage policies match with the Pod's ACL specified by the data owners. ")
public interface DataPodControllerSpec {

    @Operation(summary = "Get all data pods")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved data pods",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DataPod.class),
                            examples = @ExampleObject(name = "DataPodListExample",
                                    value = "[ { \"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"name\": \"Example Pod\", \"description\": \"An example data pod.\" } ]"))),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    Page<DataPod> getAllDataPods(
            @Parameter(description = "Filtering criteria for Data Pods", required = false, schema = @Schema(implementation = DataPodCriteria.class))
            DataPodCriteria criteria,
            @Parameter(description = "Pagination information", required = false, schema = @Schema(implementation = Pageable.class))
            Pageable pageable,
            Authentication authentication) throws ProvenAiException;

    @Operation(summary = "Get a data pod by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved data pod",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DataPod.class),
                            examples = @ExampleObject(name = "DataPodExample",
                                    value = "{ \"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"name\": \"Example Pod\", \"description\": \"An example data pod.\" }"))),
            @ApiResponse(responseCode = "204", description = "Successfully retrieved all public data pods."),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    DataPod getDataPodById(
            @Parameter(description = "ID of the data pod", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable UUID id) throws ProvenAiException;

    @Operation(summary = "Get all public data pods")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved public data pods",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DataPodPublicDTO.class)
                        )),
            @ApiResponse(responseCode = "204", description = "Successfully retrieved all public data pods."),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    Page<DataPodPublicDTO> getAllPublicDataPods(
            @Parameter(description = "Filtering criteria for Data Pods", required = false, schema = @Schema(implementation = DataPodCriteria.class))
            DataPodCriteria criteria,
            @Parameter(description = "Pagination information", required = false, schema = @Schema(implementation = Pageable.class))
            Pageable pageable,
            Authentication authentication) throws ProvenAiException;

    @Operation(summary = "Get all ACL policies for a data pod")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved ACL policies",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AclPolicies.class),
                            examples = @ExampleObject(name = "AclPoliciesListExample",
                                    value = "[ { \"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"policyName\": \"Example Policy\", \"description\": \"An example ACL policy.\", \"permissions\": [] } ]"))),
            @ApiResponse(responseCode = "204", description = "Successfully retrieved data pod ACL policies."),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    Page<AclPolicies> getAclPoliciesByDataPodId(
            @Parameter(description = "ID of the data pod", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable UUID id,
            @Parameter(description = "Pagination information", required = false, schema = @Schema(implementation = Pageable.class))
            Pageable pageable) throws ProvenAiException;

    @Operation(summary = "Create a new data pod")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created data pod",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DataPod.class),
                            examples = @ExampleObject(name = "CreateDataPodExample",
                                    value = "{\n" +
                                            "  \"id\": \"ac1sd486-6f5f-4c1e-b14d-0b4br45f5b0a\",\n" +
                                            "  \"organizationId\": \"25a554b5-d0432-469d-8340-610a4343b874\",\n" +
                                            "  \"podUniqueName\": \"Test data pod\",\n" +
                                            "  \"createdAt\": \"2024-05-09T12:00:00Z\",\n" +
                                            "  \"updatedAt\": \"2024-05-09T12:30:00Z\",\n" +
                                            "  \"aclPolicies\": [\n" +
                                            "    {\n" +
                                            "      \"policyType\": \"ATTRIBUTION_POLICY\",\n" +
                                            "      \"policyValue\": \"ONLINE_COURSE\"\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"))),
            @ApiResponse(responseCode = "204", description = "Successfully created data pod"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    DataPod createDataPod(
            @Parameter(description = "Details of the new data pod", required = true, schema = @Schema(implementation = DataPodDTO.class))
            @RequestBody DataPodDTO dataPodDto) throws ProvenAiException;

    @Operation(summary = "Update a data pod")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated data pod",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DataPod.class),
                            examples = @ExampleObject(name = "UpdatedDataPodExample",
                                    value = "{\n" +
                                            "  \"id\": \"ac1sd486-6f5f-4c1e-b14d-0b4br45f5b0a\",\n" +
                                            "  \"organizationId\": \"25a554b5-d0432-469d-8340-610a4343b874\",\n" +
                                            "  \"podUniqueName\": \"Test data pod\",\n" +
                                            "  \"createdAt\": \"2024-05-09T12:00:00Z\",\n" +
                                            "  \"updatedAt\": \"2024-05-09T12:30:00Z\",\n" +
                                            "  \"aclPolicies\": [\n" +
                                            "    {\n" +
                                            "      \"policyType\": \"ATTRIBUTION_POLICY\",\n" +
                                            "      \"policyValue\": \"ONLINE_COURSE\"\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"))),
            @ApiResponse(responseCode = "204", description = "Successfully updated data pod"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    DataPod updateDataPod(
            @Parameter(description = "ID of the data pod", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable UUID id,
            @Parameter(description = "Updated details of the data pod", required = true, schema = @Schema(implementation = DataPodDTO.class))
            @RequestBody DataPodDTO dataPodDTO) throws ProvenAiException;

    @Operation(summary = "Create an ACL policy for a data pod", description = "Add a new ACL policy to a specific data pod.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created ACL policy",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AclPolicies.class),
                            examples = @ExampleObject(name = "CreatedAclPolicyExample",
                                    value = "{ \"id\": \"123e4567-e89b-12d3-a456-426614174000\", \"policyTypeId\": \"123e4367-r29b-12d3-a456-424614374404\", \"policyOptionId\": \"671a9491-d5be-443d-b697-34bf1567cc2e\" }"))),
            @ApiResponse(responseCode = "204", description = "Successfully created ACL policy"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    AclPolicies createAclPolicy(
            @Parameter(description = "ID of the data pod", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable UUID dataPodId,
            @Parameter(description = "Details of the new ACL policy", required = true, schema = @Schema(implementation = AclPoliciesDTO.class))
            @RequestBody AclPoliciesDTO aclPoliciesDTO) throws ProvenAiException;

    @Operation(summary = "Delete an ACL policy by ID", description = "Remove a specific ACL policy from a data pod by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted ACL policy"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    void deleteAclPolicy(
            @Parameter(description = "ID of the ACL policy", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable UUID aclPolicyId) throws ProvenAiException;

    @Operation(summary = "Delete multiple ACL policies", description = "Remove multiple ACL policies from a data pod by their IDs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted ACL policies"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    void deleteAclPolicies(
            @Parameter(description = "List of ACL policy IDs", required = true, schema = @Schema(type = "array"))
            @RequestParam List<UUID> aclPolicyIds) throws ProvenAiException;


    @Operation(summary = "Create a Verifiable Credential Offer for an Agent",
            description = "Generates a Verifiable Data Ownership Credential Offer (VC Offer) for a given data pod ID."
            +"The signed VC jwt and the VC SIOP Link are received",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created Verifiable Credential Offer",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VCOfferDTO.class),
                                    examples = @ExampleObject(name = "VCOfferDTOExample",
                                            value = "{\n" +
                                                    "  \"id\": \"ac255a86-6f5f-4c1e-b11d-0b4b7dbf5b0a\",\n" +
                                                    "  \"credentialOfferUrl\": \"https://example.com/offer\",\n" +
                                                    "  \"credentialJwt\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\"\n" +
                                                    "}"))),
                    @ApiResponse(responseCode = "204", description = "Successfully generated a data ownership credential"),
                    @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
                    @ApiResponse(responseCode = "403", description = "Accessing the resource is forbidden"),
                    @ApiResponse(responseCode = "404", description = "The resource is not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping("/data-pods/{dataPodId}/credential-offer")
    public VCOfferDTO createAgentVerifiableId(
            @Parameter(description = "ID of the data pod", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable String dataPodId) throws ProvenAiException, JsonProcessingException, JSONException;

}