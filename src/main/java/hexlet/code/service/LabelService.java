package hexlet.code.service;

import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;

    public Label createLabel(LabelDTO labelDto) {
        final Label newLabel = new Label();
        newLabel.setName(labelDto.name());
        return labelRepository.save(newLabel);
    }

    public Label getLabelById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow();
    }

    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    public Label updateLabel(LabelDTO labelDto, Long id) {
        Label updateLabel = labelRepository.findById(id).get();
        updateLabel.setName(labelDto.name());
        return labelRepository.save(updateLabel);
    }

    public void deleteLabel(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow();
        labelRepository.delete(label);
    }
}
