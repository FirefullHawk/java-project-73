package hexlet.code.dto;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Set;

public record TaskDTO(@Nullable Long id,
     @NotBlank(message = "Name is required")
     String name,
     String description,
     UserDTO author,
     UserDTO executor,
     @NotNull(message = "Status is required")
     Long taskStatus,
     @Nullable
     Set<Label> labels,
     @Nullable Date createdAt) {
    public static TaskDTO toTaskDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getName(),
            task.getDescription(),
            UserDTO.toUserDTO(task.getAuthor()),
            UserDTO.toUserDTO(task.getExecutor()),
            task.getTaskStatus().getId(),
            task.getLabels(),
            task.getCreatedAt()
        );
    }
}
