package hexlet.code.dto.required;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import java.util.Set;

public record TaskRequiredDTO(
    @NotBlank(message = "Name is required")
    String name,
    String description,
    Long authorId,
    Long executorId,
    @NotNull(message = "Status is required")
    Long taskStatusId,
    @Nullable
    Set<Long> labelIds
) {
}
