package hexlet.code.dto;

import java.util.Date;

public record UserDTO(long id, String email, String firstName, String lastName, Date createdAt) {
}
