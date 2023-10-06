package hexlet.code.service.interfaces;

import hexlet.code.dto.update.StatusUpdateDTO;
import hexlet.code.model.Status;

import java.util.List;

public interface StatusServiceInterface {
    Status getStatus(long id);
    List<Status> getStatuses();
    Status createStatus(StatusUpdateDTO taskStatusDto);
    Status updateStatus(StatusUpdateDTO taskStatusDto, long id);
    void deleteStatus(long id);
}
