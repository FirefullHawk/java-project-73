package hexlet.code.service;

import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class StatusService {
    private final TaskStatusRepository taskStatusRepository;

    public TaskStatus getStatus(long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow();
    }

    public List<TaskStatus> getStatuses() {
        return taskStatusRepository.findAll();
    }

    public TaskStatus createStatus(TaskStatusDTO taskStatusDto) {
        final TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDto.name());
        taskStatusRepository.save(taskStatus);
        return taskStatus;
    }

    public TaskStatus updateStatus(TaskStatusDTO taskStatusDto, long id) {
        final TaskStatus taskStatus = getStatus(id);
        taskStatus.setName(taskStatusDto.name());
        return taskStatusRepository.save(taskStatus);
    }

    public void deleteStatus(long id) {
        final TaskStatus taskStatus = getStatus(id);
        taskStatusRepository.delete(taskStatus);
    }
}
