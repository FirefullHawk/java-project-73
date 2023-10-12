package hexlet.code.controller.api;

import hexlet.code.dto.UserDTO;
import hexlet.code.dto.required.UserRequiredDTO;
import hexlet.code.model.User;
import hexlet.code.service.UserService;
import hexlet.code.utils.NamedRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + NamedRoutes.USERS_PATH)
public class UserController {

    private final UserService userService;

    private static final String OWNER = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered"),
        @ApiResponse(responseCode = "404", description = "User with that id not found"),
        @ApiResponse(responseCode = "422", description = "User data is incorrect"),
    })
    @PostMapping
    @ResponseStatus(CREATED)
    UserDTO createUser(@Valid @RequestBody UserRequiredDTO userDto) {
        return UserDTO.toUserDTO(userService.createUser(userDto));
    }

    @Operation(summary = "Get list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = User.class)),
            description = "List of all users"),
    })
    @GetMapping
    List<UserDTO> findAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserDTO::toUserDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get specific user by his id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User with that id not found")
    })
    @GetMapping(path = "/{id}")
    UserDTO findUserById(@PathVariable long id) {
        User existedUser = userService.getUserById(id);
        return UserDTO.toUserDTO(existedUser);
    }

    @Operation(summary = "Update user by his id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated"),
        @ApiResponse(responseCode = "401", description = "Unauthorized request"),
        @ApiResponse(responseCode = "403", description = "Access denied for this user"),
        @ApiResponse(responseCode = "404", description = "User with that id not found"),
        @ApiResponse(responseCode = "422", description = "User data is incorrect")
    })
    @PutMapping(path = "/{id}")
    UserDTO updateUser(@RequestBody @Valid UserRequiredDTO dto,
                                      @PathVariable long id) {
        User updatedUser = userService.updateUser(id, dto);
        return UserDTO.toUserDTO(updatedUser);
    }

    @PreAuthorize(OWNER)
    @Operation(summary = "Delete user by his id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted"),
        @ApiResponse(responseCode = "401", description = "Unauthorized request"),
        @ApiResponse(responseCode = "403", description = "Access denied for this user"),
        @ApiResponse(responseCode = "404", description = "User with that id not found")
    })
    @DeleteMapping(path = "/{id}")
    void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
