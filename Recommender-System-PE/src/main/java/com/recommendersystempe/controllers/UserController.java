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

import com.recommendersystempe.dtos.UserDTO;
import com.recommendersystempe.service.UserService;

@RestController
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> insert(@RequestBody UserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.insert(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(
            @PageableDefault(size = 10, page = 0, sort = { "id" }, direction = Direction.ASC) Pageable peageable) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(peageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER', 'USER')")
    @PutMapping
    public ResponseEntity<String> update(@RequestBody UserDTO dto) {
        userService.update(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Profile updated successfully");
    }

    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/roles/id/{id}")
    public ResponseEntity<String> updateRole(@PathVariable("id") Long id) {
        userService.updateRole(id);
        return ResponseEntity.ok("Role updated successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
    }

}
