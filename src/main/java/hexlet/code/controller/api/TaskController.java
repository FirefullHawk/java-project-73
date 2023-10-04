package hexlet.code.controller.api;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.dto.TaskDTO;
import hexlet.code.model.Task;
import hexlet.code.service.interfaces.TaskServiceInterface;
import hexlet.code.utils.NamedRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("${base-url}" + NamedRoutes.TASKS_PATH)
public class TaskController {

    private final TaskServiceInterface taskService;

    private static final String ONLY_AUTHOR_BY_ID =
            "@TaskRepository.findTaskById(#id).get().getAuthor().getEmail().equals(authentication.getName())";

    @Operation(summary = "Create new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task has been created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO createTask(@RequestBody @Valid TaskDTO dto,
                              @AuthenticationPrincipal UserDetails authDetails) {
        Task createdTask = taskService.createTask(dto);
        return toTaskDTO(createdTask);
    }

    @Operation(summary = "Get task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task is found",
                    content = {@Content(mediaType = "application/jsom",
                            schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "404", description = "No such task found", content = @Content)})
    @GetMapping
    public TaskDTO findTasksByParams(@PathVariable(name = "id") long id) {
        return toTaskDTO(taskService.getTaskById(id));
    }

    @Operation(summary = "Get task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task is found",
                    content = {@Content(mediaType = "application/jsom",
                            schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "404", description = "No such task found", content = @Content)})
    @GetMapping(path = "/{id}")
    public TaskDTO findTaskById(@PathVariable(name = "id") long id) {
        Task existedTask = taskService.getTaskById(id);
        return toTaskDTO(existedTask);
    }

    @PreAuthorize(ONLY_AUTHOR_BY_ID)
    @Operation(summary = "Update the task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task is successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @PutMapping(path = "/{id}")
    public TaskDTO updateTask(@RequestBody @Valid TaskDTO dto,
                              @PathVariable(name = "id") long id,
                              @AuthenticationPrincipal UserDetails authDetails,
                              Authentication authentication) {

        Task updatedTask = taskService.updateTask(dto, id);
        String authenticatedEmail = authDetails.getUsername();
        if (!authenticatedEmail.equalsIgnoreCase(updatedTask.getAuthor().getEmail())) {
            throw new AccessDeniedException("Access denied");
        }
        return toTaskDTO(updatedTask);
    }

    @PreAuthorize(ONLY_AUTHOR_BY_ID)
    @Operation(summary = "Delete the task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task has been successfully deleted"),
            @ApiResponse(responseCode = "404", description = "The task is not found", content = @Content)})
    @DeleteMapping(path = "/{id}")
    public void deleteTask(@PathVariable(name = "id") long id) {
        taskService.deleteTask(id);
    }

    private TaskDTO toTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setName(task.getName());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setAuthorId(task.getAuthor().getId());

        return taskDTO;
    }
}
