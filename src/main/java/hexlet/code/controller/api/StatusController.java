package hexlet.code.controller.api;

import hexlet.code.dto.StatusDTO;
import hexlet.code.model.Status;
import hexlet.code.service.StatusService;
import hexlet.code.utils.NamedRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + NamedRoutes.STATUSES_PATH)
public class StatusController {

    private final StatusService statusService;

    @Operation(summary = "Create new task status")
    @ApiResponse(responseCode = "201", description = "New task status successfully created",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Status.class))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StatusDTO createStatus(@RequestBody @Valid StatusDTO dto) {
        Status createdStatus = statusService.createStatus(dto);
        return toStatusDTO(createdStatus);
    }

    @Operation(summary = "Get all task statuses")
    @ApiResponse(responseCode = "200", description = "All task statuses are found",
            content = @Content(schema = @Schema(implementation = Status.class)))
    @GetMapping
    public List<StatusDTO> findAllStatuses() {
        List<Status> existedStatuses = statusService.getStatuses();
        return existedStatuses.stream()
                .map(StatusController::toStatusDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get task status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task status is found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Status.class))}),
            @ApiResponse(responseCode = "404", description = "No such task status found", content = @Content)})
    @GetMapping(path = "/{id}")
    public StatusDTO findStatusById(@PathVariable(name = "id") long id) {
        Status existedStatus = statusService.getStatus(id);
        return toStatusDTO(existedStatus);
    }

    @Operation(summary = "Update the task status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task status has been successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Status.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @PutMapping(path = "/{id}")
    public StatusDTO updateStatus(@RequestBody @Valid StatusDTO dto,
                                  @PathVariable(name = "id") long id) {
        Status updatedStatus = statusService.updateStatus(dto, id);
        return toStatusDTO(updatedStatus);
    }

    @Operation(summary = "Delete the task status by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task status has been deleted"),
            @ApiResponse(responseCode = "404", description = "No such task status found")})
    @DeleteMapping(path = "/{id}")
    public void deleteStatus(@PathVariable(name = "id") long id) {
        statusService.deleteStatus(id);
    }

    private static StatusDTO toStatusDTO(Status status) {
        StatusDTO statusDTO = new StatusDTO();

        statusDTO.setName(status.getName());

        return statusDTO;
    }
}
