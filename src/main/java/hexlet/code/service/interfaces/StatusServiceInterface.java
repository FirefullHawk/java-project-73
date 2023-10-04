package hexlet.code.service.interfaces;

import hexlet.code.dto.StatusDTO;
import hexlet.code.model.Status;

import java.util.List;

public interface StatusServiceInterface {
    Status getStatus(long id);
    List<Status> getStatuses();
    Status createStatus(StatusDTO taskStatusDto);
    Status updateStatus(StatusDTO taskStatusDto, long id);
    void deleteStatus(long id);
}
