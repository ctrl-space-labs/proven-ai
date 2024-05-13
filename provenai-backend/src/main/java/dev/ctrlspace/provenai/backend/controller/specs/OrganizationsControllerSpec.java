package dev.ctrlspace.provenai.backend.controller.specs;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.OrganizationDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.OrganizationCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "Registered Organizations",
        description = "Endpoints for managing registered organizations. Full CRUD operations are supported.</br>" +
                "Organizations are entities that act as the root of ProvenAI ecosystem. Organizations can be either Natural persons or Legal Entities. " +
                "All data in ProvenAI belong to an organization, and the organization can have 1 or more users with access rights to perform actions on behalf of the organization." +
                "For Organization registration, the organization owner needs to provide their Verifiable ID in order to verify their account and details provided.")
public interface OrganizationsControllerSpec {

    @Operation(description = "Retrieves all registered organizations.",
            summary = "Retrieves all registered organizations.</br>" +
                    "Retrieves organizations based on provided criteria. </br>" +
                    "Supported criteria: organizationId, List<organizationId>, country, name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved organizations"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    Page<Organization> getAllOrganizations(
            @Parameter(description = "Filtering criteria for Data Pods", required = false, schema = @Schema(implementation = OrganizationCriteria.class))
            OrganizationCriteria criteria,
            @Parameter(description = "Pagination information", required = false, schema = @Schema(implementation = Pageable.class))
            Pageable pageable) throws ProvenAiException;

    @Operation(summary = "Get registered organization by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved organization by ID"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Organization getOrganizationById(@PathVariable UUID organizationId) throws ProvenAiException;


    @Operation(summary = "Register a new organization",
            description = "Registers a new organization with the details provided in the portal.</br>" +
                    "The organization represents both legal entities and natural persons that are part of the ProvenAI ecosystem. </br>" +
                    "The organization be used to create and manage agents, data pods, and other resources. </br>" +
                    "SSI authentication is available by providing the Verifiable ID of the organization in the request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered organization"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Organization registerOrganization(@RequestBody OrganizationDTO organizationDTO) throws JsonProcessingException;

    @Operation(summary = "Update a registered organization",
            description = "Updates a registered organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated  registered organization"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Organization updateOrganization(@PathVariable UUID organizationId, @RequestBody OrganizationDTO organizationDTO)
            throws ProvenAiException;

    @Operation(summary = "Delete a registered organization",
            description = "Deletes a registered organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted registered organization"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void deleteOrganization(@PathVariable UUID organizationId) throws ProvenAiException;


}

