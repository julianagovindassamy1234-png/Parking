package com.parking.controller;

import com.parking.dto.request.CreateUserRequest;
import com.parking.dto.response.UserResponse;
import com.parking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<UserResponse> activate(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.toggleActive(id, true));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivate(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.toggleActive(id, false));
    }
}
