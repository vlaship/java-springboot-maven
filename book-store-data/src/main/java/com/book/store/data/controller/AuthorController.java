package com.book.store.data.controller;

import com.book.store.data.dto.AuthorResponse;
import com.book.store.data.dto.CreateAuthorRequest;
import com.book.store.data.dto.ErrorResponse;
import com.book.store.data.dto.UpdateAuthorRequest;
import com.book.store.data.service.AuthorService;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/author")
@Tag(name = "Author", description = "Author controller")
public class AuthorController {

    private final AuthorService service;

    @Operation(
            operationId = "getAuthorById",
            tags = {"Author"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get Author by ID",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorResponse.class))
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
    public ResponseEntity<AuthorResponse> getAuthorById(@NotNull @PathVariable("id") UUID id) {
        AuthorResponse body = service.findById(id);
        return ResponseEntity.ok(body);
    }

    @Operation(
            operationId = "getAuthors",
            tags = {"Author"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get All Authors",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AuthorResponse.class)))
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
    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAuthors() {
        List<AuthorResponse> body = service.findAll();
        return ResponseEntity.ok(body);
    }

    @Operation(
            operationId = "createAuthor",
            tags = {"Author"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Create Author",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorResponse.class))
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
    public ResponseEntity<AuthorResponse> createAuthor(
            @Parameter(name = "CreateAuthorRequest", required = true) @NotNull @RequestBody CreateAuthorRequest request
    ) {
        AuthorResponse bookResponse = service.create(request);
        return ResponseEntity.ok(bookResponse);
    }

    @Operation(
            operationId = "updateAuthorById",
            tags = {"Author"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get Author by ID",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorResponse.class))
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
    @PatchMapping("/{authorId}")
    public ResponseEntity<AuthorResponse> updateAuthorById(
            @Parameter(name = "authorId", required = true, in = ParameterIn.PATH) @NotNull @PathVariable("authorId") UUID authorId,
            @Parameter(name = "UpdateAuthorRequest", required = true) @NotNull @RequestBody UpdateAuthorRequest request
    ) {
        AuthorResponse bookResponse = service.update(authorId, request);
        return ResponseEntity.ok(bookResponse);
    }

    @Operation(
            operationId = "deleteAuthorById",
            tags = {"Author"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Delete Author by ID",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorResponse.class))
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
    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthorById(
            @Parameter(name = "authorId", required = true, in = ParameterIn.PATH) @NotNull @PathVariable("authorId") UUID authorId
    ) {
        service.delete(authorId);
        return ResponseEntity.accepted().build();
    }
}
