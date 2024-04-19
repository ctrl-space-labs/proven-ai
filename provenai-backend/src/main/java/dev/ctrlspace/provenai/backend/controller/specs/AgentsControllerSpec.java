package dev.ctrlspace.provenai.backend.controller.specs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String getAgents(Authentication authentication);
}
