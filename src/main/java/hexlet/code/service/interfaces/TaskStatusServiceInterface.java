package hexlet.code.service.interfaces;

import hexlet.code.dto.update.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;

import java.util.List;

public interface TaskStatusServiceInterface {
    TaskStatus getStatus(long id);
    List<TaskStatus> getStatuses();
    TaskStatus createStatus(TaskStatusUpdateDTO taskStatusDto);
    TaskStatus updateStatus(TaskStatusUpdateDTO taskStatusDto, long id);
    void deleteStatus(long id);
}
