package hexlet.code.service.interfaces;

import hexlet.code.dto.update.UserUpdateDTO;
import hexlet.code.model.User;

import java.util.List;

public interface UserServiceInterface {
    User createUser(UserUpdateDTO userDto);
    User updateUser(long id, UserUpdateDTO userDto);
    String getCurrentUserId();
    User getCurrentUser();
    void deleteUser(long id);
    User getUserById(long id);
    List<User> getAllUsers();
}
