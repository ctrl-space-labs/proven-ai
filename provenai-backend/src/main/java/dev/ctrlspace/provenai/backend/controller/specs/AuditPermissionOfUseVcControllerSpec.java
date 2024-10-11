package dev.ctrlspace.provenai.backend.controller.specs;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.dtos.AuditPermissionOfUseDTO;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.AuditPermissionOfUseVcCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.List;

@Tag(name = "Permission of Use Analytics",
        description = "Endpoints for managing and analyzing permission of use audit logs. Includes retrieving analytics, auditing blockchain registration, and verifying permissions.")
public interface AuditPermissionOfUseVcControllerSpec {

    @Operation(summary = "Retrieve audit permission of use analytics",
            description = "Fetches a list of audit permission of use analytics based on the provided criteria and pagination.",
            parameters = {
                    @Parameter(name = "criteria", description = "Criteria for filtering audit permission of use records", required = true, schema = @Schema(implementation = AuditPermissionOfUseVcCriteria.class)),
                    @Parameter(name = "pageable", description = "Pagination information", required = false, schema = @Schema(implementation = Pageable.class))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved audit permission of use analytics",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuditPermissionOfUseDTO.class, type = "array"),
                                    examples = @ExampleObject(name = "AuditPermissionOfUseDTOExample",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"ownerDatapodId\": \"f6c76c78-95c0-4e43-947b-b68d4314a087\",\n" +
                                                    "    \"ownerOrganizationId\": \"f65c70d8-125b-4a69-8151-8d3bda16b2b8\",\n" +
                                                    "    \"processorAgentId\": \"dca8a9b7-f68e-4c30-8d79-c7f123ab67a6\",\n" +
                                                    "    \"processorOrganizationId\": \"e2e9d575-826e-4d0a-b9a4-9e81ae72c1d0\",\n" +
                                                    "    \"bucketStart\": \"2024-07-29T00:00:00Z\",\n" +
                                                    "    \"sumTokens\": 15000\n" +
                                                    "  }\n" +
                                                    "]"))),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Invalid criteria or pagination parameters"),
                    @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
                    @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @GetMapping("/permission-of-use-analytics")
    public List<AuditPermissionOfUseDTO> getAuditPermissionOfUseAnalytics(
            @Parameter(description = "Criteria for filtering audit permission of use records", required = true, schema = @Schema(implementation = AuditPermissionOfUseVcCriteria.class))
            @Valid AuditPermissionOfUseVcCriteria criteria,
            @Parameter(description = "Pagination information", required = false, schema = @Schema(implementation = Pageable.class))
            Pageable pageable) throws ProvenAiException;

    @Operation(summary = "Audit blockchain registration",
            description = "Registers daily usage data to the blockchain for the specified organization DID and date.",
            parameters = {
                    @Parameter(name = "organizationDid", description = "DID of the organization", required = true, schema = @Schema(type = "string")),
                    @Parameter(name = "date", description = "Date of the registration", required = true, schema = @Schema(type = "string", format = "date-time"))
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully registered daily usage data to blockchain"),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Invalid parameters"),
                    @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
                    @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping("/permission-of-use-analytics/audit/blockchain-registration")
    public void auditPermissionOfUseBlockchainRegistration(
            @Parameter(description = "DID of the organization", required = true, schema = @Schema(type = "string"))
            @RequestParam String organizationDid,
            @Parameter(description = "Date of the registration", required = true, schema = @Schema(type = "string", format = "date-time"))
            @RequestParam Instant date) throws Exception;

    @Operation(summary = "Audit permission of use verification",
            description = "Generates and verifies proofs for the specified organization DID, date, and criteria.",
            parameters = {
                    @Parameter(name = "organizationDid", description = "DID of the organization", required = true, schema = @Schema(type = "string")),
                    @Parameter(name = "date", description = "Date of the verification", required = true, schema = @Schema(type = "string", format = "date-time")),
                    @Parameter(name = "criteria", description = "Criteria for the verification", required = true, schema = @Schema(implementation = AuditPermissionOfUseVcCriteria.class))
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully generated and verified proofs"),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Invalid parameters"),
                    @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
                    @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    @PostMapping("/permission-of-use-analytics/audit/verification")
    public void auditPermissionOfUseVerification(
            @Parameter(description = "DID of the organization", required = true, schema = @Schema(type = "string"))
            @RequestParam String organizationDid,
            @Parameter(description = "Date of the verification", required = true, schema = @Schema(type = "string", format = "date-time"))
            @RequestParam Instant date,
            @Parameter(description = "Criteria for the verification", required = true, schema = @Schema(implementation = AuditPermissionOfUseVcCriteria.class))
            @RequestBody AuditPermissionOfUseVcCriteria criteria) throws Exception;
}

