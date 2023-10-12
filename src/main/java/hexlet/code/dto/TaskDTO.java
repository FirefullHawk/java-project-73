package hexlet.code.dto;

import hexlet.code.model.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.util.Set;

public record TaskDTO(@Nullable Long id,
     @NotBlank
     String name,
     String description,
     Long authorId,
     Long executorId,
     @NotNull
     Long taskStatusId,
     @Nullable
     Set<Long> labelIds) {
    public static TaskDTO toTaskDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getName(),
            task.getDescription(),
            task.getAuthor().getId(),
            task.getExecutor().getId(),
            task.getTaskStatus().getId(),
            null
        );
    }
}
