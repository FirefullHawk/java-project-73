package hexlet.code.controller.api;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDTO;
import hexlet.code.model.Task;
import hexlet.code.service.interfaces.TaskServiceInterface;
import hexlet.code.utils.NamedRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + NamedRoutes.TASKS_PATH)
public class TaskController {

    private final TaskServiceInterface taskService;
    private static final String AUTHOR = """
            @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
        """;;

    @Operation(summary = "Create new task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task has been created",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Task.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Task createTask(@RequestBody @Valid TaskDTO dto) {
        return taskService.createTask(dto);
    }

    @Operation(summary = "Get tasks by predicate or get all tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The task is found",
                content = {@Content(mediaType = "application/jsom",
                        schema = @Schema(implementation = Task.class))}),
        @ApiResponse(responseCode = "404", description = "No such task found", content = @Content)})
    @GetMapping
    Iterable<Task> getAllTask(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskService.getAllTasks(predicate);
    }

    @Operation(summary = "Get task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The task is found",
                content = {@Content(mediaType = "application/jsom",
                        schema = @Schema(implementation = Task.class))}),
        @ApiResponse(responseCode = "404", description = "No such task found", content = @Content)})
    @GetMapping(path = "/{id}")
    Task findTaskById(@PathVariable long id) {
        return taskService.getTaskById(id);
    }

    @PreAuthorize(AUTHOR)
    @Operation(summary = "Update the task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The task is successfully updated",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Task.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @PutMapping(path = "/{id}")
    public Task updateTask(@RequestBody @Valid TaskDTO dto,
                              @PathVariable long id) {

        return taskService.updateTask(dto, id);
    }

    @PreAuthorize(AUTHOR)
    @Operation(summary = "Delete the task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The task has been successfully deleted"),
        @ApiResponse(responseCode = "404", description = "The task is not found", content = @Content)})
    @DeleteMapping(path = "/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}
