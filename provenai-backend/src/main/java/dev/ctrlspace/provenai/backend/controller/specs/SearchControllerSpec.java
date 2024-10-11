package dev.ctrlspace.provenai.backend.controller.specs;

import dev.ctrlspace.provenai.backend.model.dtos.SearchResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface SearchControllerSpec {

    @Operation(summary = "Perform a search query",
            description = "Perform semantic search bases on the provided question on the provenAI registered data pods."
            +"Can be used for semantic completion in provenAI ecosystem. Returns a List of SearchResuts",
            requestBody = @RequestBody(description = "The question for semantic search",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string")),
                    required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully performed the search and returned results",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SearchResult.class, type = "array"),
                                    examples = @ExampleObject(name = "SearchResultExample",
                                            value = "[\n" +
                                                    "  {\n" +
                                                    "    \"documentSectionId\": \"ere5grdh-6rtf-4c1e-b14d-044b7dbf5b0a\",\n" +
                                                    "    \"documentId\": \"ac255a86-6f5f-4c1e-b11d-0b4b7dbf5b0a\",\n" +
                                                    "    \"iscc\": \"ISCC123456789\",\n" +
                                                    "    \"title\": \"Example Document Title\",\n" +
                                                    "    \"text\": \"This is an example of search result text that provides a brief description or content from the document.\",\n" +
                                                    "    \"documentURL\": \"https://example.com/document/doc456\",\n" +
                                                    "    \"tokens\": \"984\",\n" +
                                                    "    \"ownerName\": \"Example Organization\",\n" +
                                                    "    \"signedPermissionOfUseVc\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwZXJtaXNzaW9uIjoiY3JlYXRlIiwic2lnbmVkRGF0ZSI6IjIwMjQtMDctMDFUMTQ6MDA6MDBaIn0.SW2JxlMwM6HfSYiqg57FHRr4f5Exzv7WdyRKHteW6Go\"\n" +
                                                    "  }\n" +
                                                    "]"))),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Invalid query format"),
                    @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
                    @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    public List<SearchResult> search(
            @Parameter(description = "The question to search", required = true, schema = @Schema(type = "string"))
            @RequestBody String question,
            Authentication authentication) throws Exception;
}
