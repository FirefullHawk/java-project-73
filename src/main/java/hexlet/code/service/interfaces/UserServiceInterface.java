package hexlet.code.service.interfaces;

import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;

import java.util.List;

public interface UserServiceInterface {
    User createUser(UserDTO userDto);
    User updateUser(long id, UserDTO userDto);
    String getCurrentUserId();
    User getCurrentUser();
    void deleteUser(long id);
    User getUserById(long id);
    List<User> getAllUsers();
}
