package hexlet.code.controller;

import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.required.TaskRequiredDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.config.TestConfig;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.NamedRoutes;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static hexlet.code.config.TestConfig.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestConfig.class)

public final class TaskControllerTest {

    @Autowired
    private TestUtils utils;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @BeforeEach
    public void before() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultStatus();
        utils.regDefaultLabel();
    }

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void createTask() throws Exception {

        final TaskRequiredDTO expectedTask = buildTaskDTO();

        final var response = utils.performAuthorizedRequest(
                        post(NamedRoutes.tasksPath())
                                .content(utils.asJson(expectedTask))
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        final TaskDTO task = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(taskRepository.getReferenceById(task.id())).isNotNull();
        assertThat(expectedTask.name()).isEqualTo(task.name());
    }

    @Test
    public void getTaskById() throws Exception {

        final TaskRequiredDTO defaultTask = buildTaskDTO();
        getTaskRequest(defaultTask);

        final Task expectedTask = taskRepository.findFirstByOrderById().get();

        final var response = utils.performAuthorizedRequest(
                        get(NamedRoutes.taskPath(expectedTask.getId())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskDTO task = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(expectedTask.getId()).isEqualTo(task.id());
        assertThat(expectedTask.getName()).isEqualTo(task.name());
    }

    @Test
    public void getAllTasks() throws Exception {

        final TaskRequiredDTO defaultTask = buildTaskDTO();
        getTaskRequest(defaultTask);

        final var response = utils.performAuthorizedRequest(
                        get(NamedRoutes.tasksPath()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<TaskDTO> tasks = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final List<Task> expected = taskRepository.findAll();

        int i = 0;
        for (var task : tasks) {
            assertThat(i < expected.size());
            assertEquals(task.id(), expected.get(i).getId());
            assertEquals(task.name(), expected.get(i).getName());
            i++;
        }
    }

    @Test
    public void updateTask() throws Exception {

        final TaskRequiredDTO taskDto = buildTaskDTO();
        getTaskRequest(taskDto);

        final Task defaultTask = taskRepository.findFirstByOrderById().get();

        final Long taskId = defaultTask.getId();
        final String oldTaskName = defaultTask.getName();

        final TaskRequiredDTO taskDtoUpdate
            = buildTaskDTO("Updated task title", "Updated task description");

        var response = utils.performAuthorizedRequest(
                        put(NamedRoutes.taskPath(taskId))
                                .content(asJson(taskDtoUpdate))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskDTO updatedTask = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final String updatedTaskName = updatedTask.name();

        assertThat(taskRepository.existsById(taskId)).isTrue();
        assertThat(taskRepository.findById(taskId).get().getName()).isNotEqualTo(oldTaskName);
        assertThat(taskRepository.findById(taskId).get().getName()).isEqualTo(updatedTaskName);
        assertThat(taskRepository.findById(taskId).get().getDescription()).isEqualTo(updatedTask.description());

    }

    @Test
    public void deleteTask() throws Exception {

        final TaskRequiredDTO defaultTask = buildTaskDTO();
        getTaskRequest(defaultTask);

        final Task task = taskRepository.findFirstByOrderById().get();

        final Long taskId = task.getId();

        utils.performAuthorizedRequest(
                        delete(NamedRoutes.taskPath(taskId)))
                .andExpect(status().isOk());

        assertFalse(taskRepository.existsById(taskId));
    }

    @Test
    public void deleteTaskFail() throws Exception {

        final TaskRequiredDTO defaultTask = buildTaskDTO();
        getTaskRequest(defaultTask);

        final Long defaultTaskId = taskRepository.findFirstByOrderById().get().getId();

        final String newUsername = "new@user";

        utils.performAuthorizedRequest(
                delete(NamedRoutes.taskPath(defaultTaskId)), newUsername)
            .andExpect(status().isForbidden());
    }

    private TaskRequiredDTO buildTaskDTO() {

        User defaultUser = userRepository.findAll().stream().filter(Objects::nonNull).findFirst().get();
        TaskStatus defaultStatus = taskStatusRepository.findAll().stream().filter(Objects::nonNull).findFirst().get();
        Label defaultLabel = labelRepository.findAll().stream().filter(Objects::nonNull).findFirst().get();
        return new TaskRequiredDTO(
                "task",
                "task_description",
                UserDTO.toUserDTO(defaultUser).getId(),
                UserDTO.toUserDTO(defaultUser).getId(),
                defaultStatus.getId(),
                Set.of(defaultLabel.getId())
        );
    }

    private TaskRequiredDTO buildTaskDTO(String name, String description) {

        User defaultUser = userRepository.findAll().stream().filter(Objects::nonNull).findFirst().get();
        TaskStatus defaultStatus = taskStatusRepository.findAll().stream().filter(Objects::nonNull).findFirst().get();
        Label defaultLabel = labelRepository.findAll().stream().filter(Objects::nonNull).findFirst().get();
        return new TaskRequiredDTO(
            name,
            description,
            UserDTO.toUserDTO(defaultUser).getId(),
            UserDTO.toUserDTO(defaultUser).getId(),
            defaultStatus.getId(),
            Set.of(defaultLabel.getId())
        );
    }

    private ResultActions getTaskRequest(TaskRequiredDTO taskDto) throws Exception {
        return utils.performAuthorizedRequest(
                post(NamedRoutes.TASKS_PATH)
                        .content(asJson(taskDto))
                        .contentType(APPLICATION_JSON));
    }
}
