package hexlet.code.dto;

import hexlet.code.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.Nullable;

import java.util.Date;

public record TaskStatusDTO(@Nullable Long id,
                            @NotBlank(message = "Field 'name' must not be empty!")
                            String name,
                            @Nullable
                            Date createdAt) {
    public static TaskStatusDTO toStatusDTO(TaskStatus status) {
        return new TaskStatusDTO(status.getId(), status.getName(), status.getCreatedAt());
    }
}
