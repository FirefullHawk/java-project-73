package hexlet.code.dto;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.util.Set;

public record TaskDTO(@Nullable Long id,
     @NotBlank(message = "Name is required")
     String name,
     String description,
     User author,
     User executor,
     @NotNull(message = "Status is required")
     Long taskStatusId,
     @Nullable
     Set<Label> labels) {
    public static TaskDTO toTaskDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getName(),
            task.getDescription(),
            task.getAuthor(),
            task.getExecutor(),
            task.getTaskStatus().getId(),
            task.getLabels()
        );
    }
}
