package hexlet.code.service.interfaces;

import hexlet.code.dto.update.LabelUpdateDTO;
import hexlet.code.model.Label;

import java.util.List;

public interface LabelServiceInterface {
    Label getLabelById(Long id);
    List<Label> getAllLabels();
    Label createLabel(LabelUpdateDTO labelDto);
    Label updateLabel(LabelUpdateDTO labelDto, Long id);
    void deleteLabel(Long id);
}
