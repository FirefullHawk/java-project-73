package hexlet.code.service.interfaces;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDTO;
import hexlet.code.model.Task;

import java.util.List;

public interface TaskServiceInterface {
    Task createTask(TaskDTO taskDto);
    Task getTaskById(Long id);
    List<Task> getAllTasks(Predicate predicate);
    Task updateTask(TaskDTO taskDto, Long id);
    void deleteTask(long id);
}
