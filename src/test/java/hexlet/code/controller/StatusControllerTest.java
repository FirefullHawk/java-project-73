package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.TestConfig;
import hexlet.code.dto.update.TaskStatusUpdateDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.NamedRoutes;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TestConfig.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestConfig.class)

public final class StatusControllerTest {

    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TestUtils utils;

    @BeforeEach
    public void before() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultStatus();
    }

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void createStatus() throws Exception {

        final TaskStatusUpdateDTO expectedStatus = new TaskStatusUpdateDTO("Some status");

        final var response = utils.performAuthorizedRequest(
                        post(NamedRoutes.statusesPath())
                                .content(asJson(expectedStatus))
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        final TaskStatus status = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(taskStatusRepository.getReferenceById(status.getId())).isNotNull();
        assertThat(status.getName()).isEqualTo(expectedStatus.getName());
    }

    @Test
    public void getStatusById() throws Exception {

        final TaskStatus expectedStatus = taskStatusRepository.findAll().get(0);

        final var response = utils.performAuthorizedRequest(
                        get(NamedRoutes.statusPath(expectedStatus.getId())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(expectedStatus.getId()).isEqualTo(taskStatus.getId());
        assertThat(expectedStatus.getName()).isEqualTo(taskStatus.getName());
    }

    @Test
    public void getAllStatuses() throws Exception {

        final var response = utils.performAuthorizedRequest(
                        get(NamedRoutes.statusesPath()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<TaskStatus> taskStatuses = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final List<TaskStatus> expected = taskStatusRepository.findAll();

        int i = 0;
        for (var status : taskStatuses) {
            assertThat(i < expected.size());
            assertEquals(status.getId(), expected.get(i).getId());
            assertEquals(status.getName(), expected.get(i).getName());
            i++;
        }
    }

    @Test
    public void updateStatus() throws Exception {

        final TaskStatus defaultStatus = taskStatusRepository.findAll().get(0);
        final Long statusId = defaultStatus.getId();
        final String oldStatusName = defaultStatus.getName();

        final TaskStatusUpdateDTO newStatus = new TaskStatusUpdateDTO("Another Status");

        final var response = utils.performAuthorizedRequest(
                        put(NamedRoutes.statusPath(defaultStatus.getId()))
                                .content(asJson(newStatus))
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus updatedStatus = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final String updatedStatusName = updatedStatus.getName();

        assertThat(taskStatusRepository.existsById(statusId)).isTrue();
        assertThat(taskStatusRepository.findById(statusId).get().getName()).isNotEqualTo(oldStatusName);
        assertThat(taskStatusRepository.findById(statusId).get().getName()).isEqualTo(updatedStatusName);
    }

    @Test
    public void deleteStatus() throws Exception {

        final Long statusId = taskStatusRepository.findAll().get(0).getId();

        utils.performAuthorizedRequest(
                        delete(NamedRoutes.statusPath(statusId)))
                .andExpect(status().isOk());

        assertFalse(taskStatusRepository.existsById(statusId));
    }
}
