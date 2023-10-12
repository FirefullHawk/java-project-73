package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.TestConfig;
import hexlet.code.dto.LogInDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.NamedRoutes;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.config.TestConfig.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.LOGIN;
import static hexlet.code.utils.TestUtils.TEST_EMAIL_1;
import static hexlet.code.utils.TestUtils.TEST_EMAIL_2;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestConfig.class)

public final class UserControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils utils;

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void registration() throws Exception {

        final var response = utils.regDefaultUser()
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        final User user = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final User expectedUser = userRepository.findAll().get(0);

        assertThat(userRepository.getReferenceById(user.getId())).isNotNull();
        assertThat(expectedUser.getId()).isEqualTo(user.getId());
        assertThat(expectedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(expectedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(expectedUser.getLastName()).isEqualTo(user.getLastName());
    }

    @Test
    public void getUserById() throws Exception {

        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        final var response = utils.performAuthorizedRequest(
                        get(NamedRoutes.userPath(expectedUser.getId())))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final User user = fromJson(response.getContentAsString(), new TypeReference<>() { });
        assertThat(expectedUser.getId()).isEqualTo(user.getId());
        assertThat(expectedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(expectedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(expectedUser.getLastName()).isEqualTo(user.getLastName());
    }

    @Test
    public void getAllUsers() throws Exception {

        utils.regDefaultUser();
        final var response = utils.perform(get(NamedRoutes.usersPath()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final List<User> expected = userRepository.findAll();

        int i = 0;
        for (var user : users) {
            assertTrue(i < expected.size());
            assertEquals(user.getId(), expected.get(i).getId());
            assertEquals(user.getEmail(), expected.get(i).getEmail());
            assertEquals(user.getFirstName(), expected.get(i).getFirstName());
            assertEquals(user.getLastName(), expected.get(i).getLastName());
            i++;
        }
    }

    @Test
    public void twiceRegTheSameUserFail() throws Exception {

        utils.regDefaultUser().andExpect(status().isCreated());
        utils.regDefaultUser().andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void login() throws Exception {

        utils.regDefaultUser();
        final LogInDTO rightCredentials = new LogInDTO(TEST_EMAIL_1, "12345");

        final var loginRequest = post(LOGIN).content(asJson(rightCredentials)).contentType(APPLICATION_JSON);
        utils.perform(loginRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void loginFails() throws Exception {

        utils.regDefaultUser();
        final LogInDTO wrongCredentials = new LogInDTO(TEST_EMAIL_2, "password");

        final var loginRequest = post(LOGIN).content(asJson(wrongCredentials)).contentType(APPLICATION_JSON);
        utils.perform(loginRequest)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateUser() throws Exception {

        utils.regDefaultUser();
        final UserDTO newUserDto = new UserDTO(
            null,
                TEST_EMAIL_2,
                "new name",
                "new last name",
                null,
            "new pwd");

        final Long userId = userRepository.findByEmail(TEST_EMAIL_1).get().getId();

        final var response = utils.performAuthorizedRequest(
                        put(NamedRoutes.userPath(userId))
                                .content(asJson(newUserDto))
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        final User user = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final User expectedUser = userRepository.findAll().get(0);

        assertThat(user.getId()).isEqualTo(expectedUser.getId());
        assertThat(expectedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(expectedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(expectedUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(userRepository.existsById(userId)).isTrue();
        assertNull(userRepository.findByEmail(TEST_EMAIL_1).orElse(null));
        assertNotNull(userRepository.findByEmail(TEST_EMAIL_2).orElse(null));
    }

    @Test
    public void deleteUser() throws Exception {

        utils.regDefaultUser();
        final Long userId = userRepository.findByEmail(TEST_EMAIL_1).get().getId();

        utils.performAuthorizedRequest(
                        delete(NamedRoutes.userPath(userId)))
                .andExpect(status().isOk());

        assertFalse(userRepository.existsById(userId));

    }

    @Test
    public void deleteUserFails() throws Exception {

        utils.regDefaultUser();
        final UserDTO newUserDto = new UserDTO(
            null,
                TEST_EMAIL_2,
                "fname",
                "lname",
                null,
                "pwd");

        utils.regNewInstance(NamedRoutes.usersPath(), newUserDto);

        final Long defaultUserId = userRepository.findByEmail(TEST_EMAIL_1).get().getId();
        final Long newUserId = userRepository.findByEmail(TEST_EMAIL_2).get().getId();

        utils.performAuthorizedRequest(
                        delete(NamedRoutes.userPath(defaultUserId)))
                .andExpect(status().isOk());

        utils.performAuthorizedRequest(
                        delete(NamedRoutes.userPath(newUserId)))
                .andExpect(status().isForbidden());
    }
}
