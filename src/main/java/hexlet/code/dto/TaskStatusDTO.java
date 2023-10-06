package hexlet.code.dto;

import java.util.Date;

public record TaskStatusDTO(long id, String name, Date createdAt) {
}
