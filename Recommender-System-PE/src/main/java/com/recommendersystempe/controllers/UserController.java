package com.recommendersystempe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recommendersystempe.controllers.exception.StandardError;
import com.recommendersystempe.controllers.exception.ValidationError;
import com.recommendersystempe.dtos.UserDTO;
import com.recommendersystempe.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))) })
    @Operation(summary = "Insert a user", description = "Insert a new User. Accessible to everyone.", tags = {
            "Auth" })
    public ResponseEntity<UserDTO> insert(@RequestBody UserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.insert(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))) })
    @Operation(summary = "Find all users", description = "Retrieve a paginated list of all user. Only accessible by ADMIN and MASTER roles.", tags = {
            "Auth" })
    public ResponseEntity<Page<UserDTO>> findAll(
            @PageableDefault(size = 10, page = 0, sort = { "id" }, direction = Direction.ASC) Pageable peageable) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(peageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping("/id/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully find by id", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class)))
    })
    @Operation(summary = "Find by user id", description = "Retrieve user by their ID. Only accessible by ADMIN and MASTER roles.", tags = {
            "Auth" })
    public ResponseEntity<UserDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'USER')")
    @PutMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully update", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class))) })
    @Operation(summary = "Update User", description = "Update User, only for Admin and Master", tags = {
            "Auth" })
    public ResponseEntity<String> update(@RequestBody UserDTO dto) {
        userService.update(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Profile updated successfully");
    }

    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/roles/id/{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully update", content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class))) })
@Operation(summary = "Update Role", description = "Update Role, only for Master", tags = {
        "Auth" })
    public ResponseEntity<String> updateRole(@PathVariable("id") Long id) {
        userService.updateRole(id);
        return ResponseEntity.ok("Role updated successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @DeleteMapping("/id/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ValidationError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = StandardError.class))) })
    @Operation(summary = "Delete User", description = "Delete a user by its ID. Only accessible by ADMIN and MASTER roles.", tags = {
        "Auth" })
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
    }

}
