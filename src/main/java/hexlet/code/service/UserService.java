package hexlet.code.service;

import hexlet.code.dto.required.UserRequiredDTO;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserRequiredDTO userDto) {
        final User user = new User();

        return userRepository.save(merge(user, userDto));
    }

    public User updateUser(final long id, final UserRequiredDTO userDto) {
        final User userToUpdate = userRepository.findById(id).get();

        return userRepository.save(merge(userToUpdate, userDto));
    }

    public void deleteUser(long id) {
        final User userToDelete = userRepository.findById(id).get();
        userRepository.delete(userToDelete);
    }
    public String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserId()).get();
    }

    private User merge(User user, UserRequiredDTO userDto) {
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return user;
    }
}
