package dev.ctrlspace.provenai.backend.controller.specs;

import dev.ctrlspace.provenai.backend.model.Organization;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Registered Organizations", description = "Endpoints for managing registered organizations")
public interface OrganizationsControllerSpec {

    @Operation(summary = "Get all registered organizations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved organizations"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public String getOrganizations();

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

    public Organization registerOrganization(Organization organization);
}
