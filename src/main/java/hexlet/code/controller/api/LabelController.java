package hexlet.code.controller.api;

import hexlet.code.dto.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.service.LabelService;
import hexlet.code.utils.NamedRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + NamedRoutes.LABELS_PATH)
public class LabelController {

    private final LabelService labelService;

    @Operation(summary = "Create new label")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "The label has been successfully created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class))})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    LabelDTO createLabel(@RequestBody @Valid LabelDTO dto) {
        return LabelDTO.toLabelDTO(labelService.createLabel(dto));
    }

    @Operation(summary = "Get all labels")
    @ApiResponse(responseCode = "200", description = "All labels are found",
            content = @Content(schema = @Schema(implementation = Label.class)))
    @GetMapping
    List<LabelDTO> findAllLabels() {
        return labelService.getAllLabels()
                .stream()
                .map(LabelDTO::toLabelDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The label is found",
                    content = {@Content(mediaType = "application/jsom",
                            schema = @Schema(implementation = Label.class))}),
        @ApiResponse(responseCode = "404", description = "No such label found", content = @Content)})
    @GetMapping(path = "/{id}")
    LabelDTO findLabelById(@PathVariable long id) {
        return LabelDTO.toLabelDTO(labelService.getLabelById(id));
    }

    @Operation(summary = "Update the label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The label is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class))}),
        @ApiResponse(responseCode = "422", description = "Invalid request", content = @Content)})
    @PutMapping(path = "/{id}")
    LabelDTO updateLabel(@RequestBody @Valid LabelDTO dto,
                                @PathVariable long id) {
        Label updatedLabel = labelService.updateLabel(dto, id);
        return LabelDTO.toLabelDTO(updatedLabel);
    }

    @Operation(summary = "Delete the label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label has been successfully deleted"),
        @ApiResponse(responseCode = "404", description = "No such label found")})
    @DeleteMapping(path = "/{id}")
    void deleteLabel(@PathVariable long id) {
        labelService.deleteLabel(id);
    }
}
