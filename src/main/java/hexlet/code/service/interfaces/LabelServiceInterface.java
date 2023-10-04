package hexlet.code.service.interfaces;

import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import java.util.List;

public interface LabelServiceInterface {
    Label getLabelById(Long id);
    List<Label> getAllLabels();
    Label createLabel(LabelDTO labelDto);
    Label updateLabel(LabelDTO labelDto, Long id);
    void deleteLabel(Long id);
}