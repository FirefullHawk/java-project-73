package hexlet.code.dto.required;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequiredDTO(
    @NotBlank(message = "Field 'email' must not be empty!")
    @Email(message = "Email format is invalid!")
    String email,

    @NotBlank(message = "Field 'firstName' must not be empty!")
    String firstName,

    @NotBlank(message = "Field 'lastName' must not be empty!")
    String lastName,

    @NotBlank(message = "Field 'password' must not be empty!")
    @Size(min = 3, message = "Password length must be at least 3 characters!")
    String password) {
}
