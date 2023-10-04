package hexlet.code.controller;

import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.config.TestConfig;
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
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.config.TestConfig.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestConfig.class)

public class LabelControllerTest {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TestUtils utils;

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
    public void createLabel() throws Exception {

        final LabelDTO expectedLabel = new LabelDTO("New label");

        final var response = utils.performAuthorizedRequest(
                        post(NamedRoutes.labelsPath())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(expectedLabel)))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        final Label label = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(labelRepository.getReferenceById(label.getId())).isNotNull();
        assertThat(label.getName()).isEqualTo(expectedLabel.getName());
    }

    @Test
    public void getLabel() throws Exception {

        final Label expectedLabel = labelRepository.findFirstByOrderById().get();

        final var response = utils.performAuthorizedRequest(
                        get(NamedRoutes.labelPath(expectedLabel.getId())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Label label = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(label.getId()).isEqualTo(expectedLabel.getId());
        assertThat(label.getName()).isEqualTo(expectedLabel.getName());
    }

    @Test
    public void getAllLabels() throws Exception {

        final LabelDTO newLabel = new LabelDTO("New label");
        utils.regNewInstance(NamedRoutes.labelsPath(), newLabel);

        final var response = utils.performAuthorizedRequest(
                        get(NamedRoutes.labelsPath()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Label> labels = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final List<Label> expected = labelRepository.findAll();

        int i = 0;
        for (var label : labels) {
            assertThat(i < expected.size());
            assertEquals(label.getId(), expected.get(i).getId());
            assertEquals(label.getName(), expected.get(i).getName());
            i++;
        }
    }

    @Test
    public void updateLabel() throws Exception {

        final Label defaultLabel = labelRepository.findFirstByOrderById().get();
        final Long labelId = defaultLabel.getId();
        final String oldLabelName = defaultLabel.getName();

        final LabelDTO newLabel = new LabelDTO("Updated label");

        final var response = utils.performAuthorizedRequest(
                        put(NamedRoutes.labelPath(labelId))
                                .content(asJson(newLabel))
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Label updatedLabel = fromJson(response.getContentAsString(), new TypeReference<>() { });
        final String updatedLabelName = updatedLabel.getName();

        assertThat(labelRepository.existsById(labelId)).isTrue();
        assertThat(labelRepository.findById(labelId).get().getName()).isNotEqualTo(oldLabelName);
        assertThat(labelRepository.findById(labelId).get().getName()).isEqualTo(updatedLabelName);
    }

    @Test
    public void deleteLabel() throws Exception {

        final Long defaultLabelId = labelRepository.findFirstByOrderById().get().getId();

        utils.performAuthorizedRequest(
                        delete(NamedRoutes.labelPath(defaultLabelId)))
                .andExpect(status().isOk());

        assertFalse(labelRepository.existsById(defaultLabelId));
    }
}