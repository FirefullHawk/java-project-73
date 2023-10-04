package hexlet.code.service;
import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.interfaces.LabelServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LabelService implements LabelServiceInterface {

    private final LabelRepository labelRepository;
    @Override
    public Label createLabel(LabelDTO labelDto) {
        Label newLabel = new Label();
        newLabel.setName(labelDto.getName());
        return labelRepository.save(newLabel);
    }

    @Override
    public Label getLabelById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @Override
    public Label updateLabel(LabelDTO labelDto, Long id) {
        Label updateLabel = labelRepository.findById(id).get();
        updateLabel.setName(labelDto.getName());
        return labelRepository.save(updateLabel);
    }

    @Override
    public void deleteLabel(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow();
        labelRepository.delete(label);
    }
}
