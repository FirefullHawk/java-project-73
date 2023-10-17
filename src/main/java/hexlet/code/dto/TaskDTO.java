package hexlet.code.dto;

import hexlet.code.model.Task;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public record TaskDTO(@Nullable Long id,
     String name,
     String description,
     UserDTO author,
     UserDTO executor,
     TaskStatusDTO taskStatus,
     @Nullable
     Set<LabelDTO> labels,
     @Nullable Date createdAt) {
    public static TaskDTO toTaskDTO(Task task) {
        return new TaskDTO(
            task.getId(),
            task.getName(),
            task.getDescription(),
            UserDTO.toUserDTO(task.getAuthor()),
            UserDTO.toUserDTO(task.getExecutor()),
            TaskStatusDTO.toStatusDTO(task.getTaskStatus()),
            task.getLabels().stream().map(LabelDTO::toLabelDTO).collect(Collectors.toSet()),
            task.getCreatedAt()
        );
    }
}
