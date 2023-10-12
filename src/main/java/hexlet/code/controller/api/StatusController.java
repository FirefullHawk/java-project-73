package hexlet.code.controller.api;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.StatusService;
import hexlet.code.utils.NamedRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + NamedRoutes.STATUSES_PATH)
public class StatusController {

    private final StatusService statusService;

    @Operation(summary = "Create new task status")
    @ApiResponse(responseCode = "201", description = "New task status successfully created",
        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TaskStatus.class))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TaskStatusDTO createStatus(@RequestBody @Valid TaskStatusDTO dto) {
        return TaskStatusDTO.toStatusDTO(statusService.createStatus(dto));
    }

    @Operation(summary = "Get all task statuses")
    @ApiResponse(responseCode = "200", description = "All task statuses are found",
        content = @Content(schema = @Schema(implementation = TaskStatus.class)))
    @GetMapping
    List<TaskStatusDTO> findAllStatuses() {
        List<TaskStatus> existedStatuses = statusService.getStatuses();
        return existedStatuses.stream()
                .map(TaskStatusDTO::toStatusDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get task status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The task status is found",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskStatus.class))}),
        @ApiResponse(responseCode = "404", description = "No such task status found", content = @Content)})
    @GetMapping(path = "/{id}")
    TaskStatusDTO findStatusById(@PathVariable long id) {
        TaskStatus existedStatus = statusService.getStatus(id);
        return TaskStatusDTO.toStatusDTO(existedStatus);
    }

    @Operation(summary = "Update the task status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The task status has been successfully updated",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskStatus.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @PutMapping(path = "/{id}")
    TaskStatusDTO updateStatus(@RequestBody @Valid TaskStatusDTO dto,
                               @PathVariable long id) {
        TaskStatus updatedStatus = statusService.updateStatus(dto, id);
        return TaskStatusDTO.toStatusDTO(updatedStatus);
    }

    @Operation(summary = "Delete the task status by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task status has been deleted"),
        @ApiResponse(responseCode = "404", description = "No such task status found")})
    @DeleteMapping(path = "/{id}")
    void deleteStatus(@PathVariable long id) {
        statusService.deleteStatus(id);
    }
}
