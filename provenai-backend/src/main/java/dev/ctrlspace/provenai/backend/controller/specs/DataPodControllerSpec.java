package dev.ctrlspace.provenai.backend.controller.specs;

import dev.ctrlspace.provenai.backend.exceptions.ProvenAiException;
import dev.ctrlspace.provenai.backend.model.DataPod;
import dev.ctrlspace.provenai.backend.model.dtos.criteria.DataPodCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Tag(name = "Data Pods",
        description = "Endpoints for managing data pods. Full CRUD operations are supported.</br>" +
        "Data Pods can be created by organizations to store metadata of the actual data like that location in an external system and an Access Control List (ACL) specifying the Policies under which the data can be accessed. " +
        "Data in a specific Pods can be shared with AI Agents only if the Agent's usage policies match with the Pod's ACL specified by the data owners. ")
public interface DataPodControllerSpec {

    @Operation(summary = "Get all data pods")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved data pods"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    Page<DataPod> getAll(
            @Parameter(description = "Filtering criteria for Data Pods", required = false, schema = @Schema(implementation = DataPodCriteria.class))
            DataPodCriteria criteria,
            @Parameter(description = "Pagination information", required = false, schema = @Schema(implementation = Pageable.class))
            Pageable pageable) throws ProvenAiException;

    @Operation(summary = "Get a data pod by id")
    public DataPod getById(UUID id) throws ProvenAiException;

    @Operation(summary = "Create a new data pod")
    public DataPod create(DataPod dataPod);


}
