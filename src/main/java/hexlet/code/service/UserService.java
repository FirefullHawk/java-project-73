package hexlet.code.service;

import hexlet.code.dto.user.RequestDTO;
import hexlet.code.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface UserService {

    @Transactional
    User createUser(RequestDTO dto);

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserReferenceById(long id);

    User getUserByEmail(String email);

    @Transactional
    User updateUser(long id, RequestDTO dto, UserDetails authDetails);

    @Transactional
    void deleteUser(long id, UserDetails authDetails);
}
