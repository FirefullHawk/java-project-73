package hexlet.code.dto;

import hexlet.code.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private Date createdAt;

    public UserDTO(final Long id) {
        this.id = id;
    }

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO(user.getId());

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setCreatedAt(user.getCreatedAt());

        return userDTO;
    }
}
