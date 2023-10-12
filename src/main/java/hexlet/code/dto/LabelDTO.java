package hexlet.code.dto;

import hexlet.code.model.Label;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.Nullable;

import java.util.Date;

public record LabelDTO(@Nullable Long id,
                       @NotBlank(message = "Field 'name' must not be empty!") String name,
                       @Nullable Date createdAt) {
    public static LabelDTO toLabelDTO(Label label) {
        return new LabelDTO(label.getId(), label.getName(), label.getCreatedAt());
    }
}
