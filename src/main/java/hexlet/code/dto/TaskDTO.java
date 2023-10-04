package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    @NotBlank
    private String name;
    private String description;
    private Long authorId;
    private Long executorId;
    @NotNull
    private Long taskStatusId;
    private Set<Long> labelIds;
}
