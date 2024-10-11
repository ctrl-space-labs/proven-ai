package dev.ctrlspace.provenai.backend.controller.specs;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.PolicyOption;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PolicyControllerSpec {

    @Operation(summary = "Retrieve policy options by policy type name",
            description = "Fetches a list of policy options based on the specified policy type name.",
            parameters = {
                    @Parameter(name = "policyTypeName", description = "The name of the policy type for which options are to be retrieved", required = true, schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved policy options",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PolicyOption.class, type = "array"),
                                    examples = @ExampleObject(name = "PolicyOptionsExample",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"id\": \"1e54cbe-a735-4334-b238-5032324d44489\",\n" +
                                                    "    \"policyTypeId\": \"ce05553-e8f4-4267-9e4-d594ggd4faef\",\n" +
                                                    "    \"name\": \"SCHOOL_ASSISTANT\",\n" +
                                                    "    \"description\": \"Allow the content to be used from an AI educator assistant\"\n" +
                                                    "  }\n" +
                                                    "]"))),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Invalid policy type name"),
                    @ApiResponse(responseCode = "404", description = "Not Found: Policy type does not exist"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    public List<PolicyOption> getPolicyOptionsByTypeName(
            @Parameter(description = "The name of the policy type for which options are to be retrieved", required = true, schema = @Schema(type = "string"))
            @RequestParam String policyTypeName) throws ProvenAiException;
}
