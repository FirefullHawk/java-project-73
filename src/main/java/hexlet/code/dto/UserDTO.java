package hexlet.code.dto;

import hexlet.code.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

import java.util.Date;

public record UserDTO(@Nullable Long id,
                      @NotBlank(message = "Field 'email' must not be empty!")
                      @Email(message = "Email format is invalid!")
                      String email,
                      @NotBlank(message = "Field 'firstName' must not be empty!")
                      String firstName,
                      @NotBlank(message = "Field 'lastName' must not be empty!")
                      String lastName,
                      @Nullable
                      Date createdAt,
                      @Nullable
                      @NotBlank(message = "Field 'password' must not be empty!")
                      @Size(min = 3, message = "Password length must be at least 3 characters!")
                      String password) {
    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getCreatedAt(),
            null);
    }
}
