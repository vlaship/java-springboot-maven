package com.book.store.data.controller;

import com.book.store.data.dto.ErrorResponse;
import com.book.store.data.dto.UserRequest;
import com.book.store.data.dto.UserResponse;
import com.book.store.data.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "User", description = "User controller")
public class UserController {

    private final UserService userService;

    @Operation(
            operationId = "createUser",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Create User",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            })
            })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Parameter(name = "UserRequest", required = true) @NotNull @Valid @RequestBody UserRequest userRequest
    ) {
        UserResponse user = userService.create(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(
            operationId = "updateUser",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update User",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            })
            })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(name = "id", required = true, in = ParameterIn.PATH) @NotNull @PathVariable("id") UUID id,
            @Parameter(name = "UserRequest", required = true) @NotNull @Valid @RequestBody UserRequest userRequest
    ) {
        UserResponse user = userService.update(id, userRequest);
        return ResponseEntity.ok(user);
    }

    @Operation(
            operationId = "deleteUser",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Delete User",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            })
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(name = "id", required = true, in = ParameterIn.PATH) @NotNull @PathVariable("id") UUID id
    ) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            operationId = "getUserByUsername",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get User by Username",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            })
            })
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@Valid @NotNull @NotBlank @Size(min = 5, max = 50) @PathVariable("username") String username) {
        UserResponse user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @Operation(
            operationId = "getUserById",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get User by ID",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            })
            })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(name = "id", required = true, in = ParameterIn.PATH) @NotNull @PathVariable("id") UUID id
    ) {
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
            operationId = "existsByUsername",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Check if username exists",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Boolean.class))
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            })
            })
    @GetMapping("/exists/username")
    public ResponseEntity<Boolean> existsByUsername(@Valid @NotNull @NotBlank @Size(min = 5, max = 50) @RequestParam("username") String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @Operation(
            operationId = "existsByEmail",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Check if email exists",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Boolean.class))
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))
                            })
            })
    @GetMapping("/exists/email")
    public ResponseEntity<Boolean> existsByEmail(@Valid @NotNull @Email @RequestParam("email") String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}
