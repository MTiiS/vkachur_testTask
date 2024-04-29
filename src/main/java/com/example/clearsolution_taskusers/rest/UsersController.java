package com.example.clearsolution_taskusers.rest;

import com.example.clearsolution_taskusers.dto.UserCreateRequest;
import com.example.clearsolution_taskusers.dto.UserFilterRequest;
import com.example.clearsolution_taskusers.dto.UserPartialUpdateRequest;
import com.example.clearsolution_taskusers.dto.UserResponse;
import com.example.clearsolution_taskusers.model.User;
import com.example.clearsolution_taskusers.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse<User>> findUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        UserResponse<User> response = new UserResponse<>(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<UserResponse<List<User>>> getAllUsers(@Valid UserFilterRequest request) {
        List<User> users = userService.getUsersByFilter(request);
        UserResponse<List<User>> usersListResponse = new UserResponse<>(users);
        return new ResponseEntity<>(usersListResponse, HttpStatus.OK);
    }


    @PostMapping(value = "/")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userService.createUser(request);
        if (user!=null) {
            UriComponents uriBuilder = UriComponentsBuilder.fromPath("/api/users/{id}").buildAndExpand(user.getId());
            String newUserUrl = uriBuilder.toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(newUserUrl));
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateRequest request) {
        userService.updateUser(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> partialUpdateUser(@PathVariable Long id, @Valid @RequestBody UserPartialUpdateRequest request) {
        userService.partialUpdateUser(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
