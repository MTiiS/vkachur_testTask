package com.example.clearsolution_taskusers.service;

import com.example.clearsolution_taskusers.dto.UserCreateRequest;
import com.example.clearsolution_taskusers.dto.UserFilterRequest;
import com.example.clearsolution_taskusers.dto.UserPartialUpdateRequest;
import com.example.clearsolution_taskusers.model.User;

import java.util.List;

public interface UserService {
    User createUser(UserCreateRequest request);


    void deleteUser(Long id);

    User updateUser(Long id, UserCreateRequest request);

    User partialUpdateUser(Long id, UserPartialUpdateRequest request);

    User findUserById(Long id);

    List<User> getUsersByFilter(UserFilterRequest request);
}
