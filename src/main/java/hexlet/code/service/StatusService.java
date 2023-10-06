package hexlet.code.service;

import hexlet.code.dto.update.StatusUpdateDTO;
import hexlet.code.model.Status;
import hexlet.code.repository.StatusRepository;
import hexlet.code.service.interfaces.StatusServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class StatusService implements StatusServiceInterface {
    private final StatusRepository taskStatusRepository;

    @Override
    public Status getStatus(long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public List<Status> getStatuses() {
        return taskStatusRepository.findAll();
    }

    @Override
    public Status createStatus(StatusUpdateDTO taskStatusDto) {
        final Status taskStatus = new Status();
        taskStatus.setName(taskStatusDto.getName());
        taskStatusRepository.save(taskStatus);
        return taskStatus;
    }

    @Override
    public Status updateStatus(StatusUpdateDTO taskStatusDto, long id) {
        final Status taskStatus = getStatus(id);
        taskStatus.setName(taskStatusDto.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public void deleteStatus(long id) {
        final Status taskStatus = getStatus(id);
        taskStatusRepository.delete(taskStatus);
    }
}
