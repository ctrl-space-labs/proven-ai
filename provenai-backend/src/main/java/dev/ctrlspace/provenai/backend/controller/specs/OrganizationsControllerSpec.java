package dev.ctrlspace.provenai.backend.controller.specs;

import dev.ctrlspace.provenai.backend.model.Organization;
import dev.ctrlspace.provenai.backend.model.dtos.OrganizationDTO;
import dev.ctrlspace.provenai.backend.model.dtos.VerifiablePresentationResponse;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.OrganizationCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "Registered Organizations", description = "Endpoints for managing registered organizations")
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
    Page<Organization> getAllOrganizations(OrganizationCriteria criteria, Pageable pageable) throws Exception;

    @Operation(summary = "Get registered organization by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved organization by ID"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Optional<Organization> getOrganizationById(@PathVariable UUID organizationId) throws Exception;

    @Operation(summary = "Register a new organization",
            description = "Registers a new organization with the details provided in the portal.</br>" +
                    "The organization represents both legal entities and natural persons that are part of the ProvenAI ecosystem. </br>" +
                    "The organization is a legal entity that can be used to create and manage agents, data pods, and other resources. </br>" +
                    "SSI authentication is available by providing the Verifiable ID of the organization in the request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered organization"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Organization registerOrganization(@RequestBody OrganizationDTO organizationDTO) throws Exception;

    @Operation(summary = "Update a registered organization",
            description = "Updates a registered organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated  registered organization"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Organization updateOrganization(@PathVariable UUID organizationId, @RequestBody OrganizationDTO organizationDTO) throws Exception;

    @Operation(summary = "Delete a registered organization",
            description = "Deletes a registered organization.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted registered organization"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void deleteOrganization(@PathVariable UUID organizationId) throws Exception;


    @Operation(summary = "Present the Verifiable Presentation ID of an organization",
            description = "Presents the Verifiable Presentation ID of an organization provided an organization ID." +
                    "The Verifiable Presentation ID is exported as a JSON object." +
                    "The credential offer URL is exported. It can be copied to a web wallet to import a verifiable credential."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted registered organization"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    VerifiablePresentationResponse getOrganizationVerifiablePresentation(@PathVariable UUID organizationId) throws Exception;



    }

