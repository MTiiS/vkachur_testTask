package com.example.clearsolution_taskusers.service;

import com.example.clearsolution_taskusers.dto.UserCreateRequest;
import com.example.clearsolution_taskusers.dto.UserFilterRequest;
import com.example.clearsolution_taskusers.dto.UserPartialUpdateRequest;
import com.example.clearsolution_taskusers.mapping.UserMapper;
import com.example.clearsolution_taskusers.model.User;
import com.example.clearsolution_taskusers.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ValidRequest_ReturnsCreatedUser() {
        // Create a mock UserCreateRequest object
        UserCreateRequest request = new UserCreateRequest(
                "Andriy",
                "Moroz",
                "Andriy@example.com",
                LocalDate.of(1990, 1, 1),
                null, null);

        // Create a mock User object
        User user = new User(1L,
                "Andriy",
                "Moroz",
                "Andriy@example.com",
                LocalDate.of(1990, 1, 1),
                null, null);

        // Set up the mocks behavior
        when(userMapper.convertCreateRequestToEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // Call the createUser method
        User createdUser = userService.createUser(request);

        // Verify that the expected user is returned
        assertEquals(user, createdUser);

        // Verify that mapper and repository methods were called
        verify(userMapper, times(1)).convertCreateRequestToEntity(request);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser_UserExists_DeletesUser() {
        // Set up the user ID to delete
        Long userId = 1L;

        // Mock the findById method behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        // Call the deleteUser method
        userService.deleteUser(userId);

        // Verify that deleteById method was called with the correct ID
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_UserNotExists_ThrowsNoSuchElementException() {
        // Set up the ID of a non-existing user to delete
        Long userId = 1L;

        // Mock the findById method behavior
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Attempt to call the deleteUser method and expect a NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void updateUser_UserExists_ReturnsUpdatedUser() {
        // Create a mock UserCreateRequest object
        UserCreateRequest request = new UserCreateRequest(
                "Andriy",
                "Moroz",
                "Andriy@example.com",
                LocalDate.of(1990, 1, 1),
                null,
                null);

        // Create a mock User object
        User existingUser = new User(1L,
                "Andriy",
                "Moroz",
                "Andriy@example.com",
                LocalDate.of(1990, 1, 1),
                null,
                null);

        // Set up the mocks behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Call the updateUser method
        User updatedUser = userService.updateUser(1L, request);

        // Verify that findById method was called with the correct ID
        verify(userRepository, times(1)).findById(1L);

        // Verify that save method was called
        verify(userRepository, times(1)).save(existingUser);

        // Verify that the user was updated
        assertEquals(request.getFirstName(), updatedUser.getFirstName());
        assertEquals(request.getLastName(), updatedUser.getLastName());
        assertEquals(request.getEmail(), updatedUser.getEmail());
        assertEquals(request.getDateOfBirth(), updatedUser.getDateOfBirth());
        assertEquals(request.getAddress(), updatedUser.getAddress());
        assertEquals(request.getPhoneNumber(), updatedUser.getPhoneNumber());
    }

    @Test
    void updateUser_UserNotExists_ThrowsNoSuchElementException() {

        UserCreateRequest request = new UserCreateRequest("Andriy",
                "Moroz",
                "Andriy@example.com",
                LocalDate.of(1990, 1, 1),
                null,
                null);

        // Set up the ID of a non-existing user to update
        Long userId = 1L;

        // Mock the findById method behavior
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Attempt to call the updateUser method and expect a NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> userService.updateUser(userId, request));
    }

    @Test
    void partialUpdateUser_UserExists_ReturnsUpdatedUser() {

        UserPartialUpdateRequest request = new UserPartialUpdateRequest(
                "NewAndriy",
                null,
                null,
                null,
                null,
                null);

        User existingUser = new User(
                1L,
                "Andriy",
                "Moroz",
                "Andriy@example.com",
                LocalDate.of(1990, 1, 1),
                null, null);

        User expectedUpdatedUser = new User(
                1L, "NewAndriy",
                "Moroz",
                "Andriy@example.com",
                LocalDate.of(1990, 1, 1),
                null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));


        User updatedUser = userService.partialUpdateUser(1L, request);

        verify(userRepository, times(1)).findById(1L);

        verify(userRepository, times(1)).save(any(User.class));

        assertEquals(expectedUpdatedUser, updatedUser);
        assertEquals(expectedUpdatedUser.getFirstName(), updatedUser.getFirstName());
        assertEquals(existingUser.getLastName(), updatedUser.getLastName());
        assertEquals(existingUser.getEmail(), updatedUser.getEmail());
        assertEquals(existingUser.getDateOfBirth(), updatedUser.getDateOfBirth());
        assertEquals(existingUser.getAddress(), updatedUser.getAddress());
        assertEquals(existingUser.getPhoneNumber(), updatedUser.getPhoneNumber());
    }

    @Test
    void partialUpdateUser_UserNotExists_ThrowsNoSuchElementException() {
        // Create a mock UserPartialUpdateRequest object
        UserPartialUpdateRequest request = new UserPartialUpdateRequest("NewJohn", null, null, null, null, null);

        // Set up the ID of a non-existing user to update
        Long userId = 1L;

        // Mock the findById method behavior
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Attempt to call the partialUpdateUser method and expect a NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> userService.partialUpdateUser(userId, request));
    }

    @Test
    void findUserById_UserExists_ReturnsUser() {
        // Set up the ID of an existing user
        Long userId = 1L;

        // Create a mock User object
        User existingUser = new User(
                userId,
                "Andriy",
                "Moroz",
                "Andriy@example.com",
                LocalDate.of(1990, 1, 1),
                null,
                null);

        // Mock the findById method behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Call the findUserById method
        User foundUser = userService.findUserById(userId);

        // Verify that the correct user is returned
        assertEquals(existingUser, foundUser);
    }

    @Test
    void findUserById_UserNotExists_ThrowsNoSuchElementException() {
        // Set up the ID of a non-existing user
        Long userId = 1L;

        // Mock the findById method behavior
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Attempt to call the findUserById method and expect a NoSuchElementException
        assertThrows(NoSuchElementException.class, () -> userService.findUserById(userId));
    }

    @Test
    void getUsersByFilter_ReturnsListOfUsers() {
        // Create a UserFilterRequest with some filter criteria
        UserFilterRequest request = new UserFilterRequest();
        request.setDateOfBirthFrom(LocalDate.parse("1990-01-01"));
        request.setDateOfBirthTo(LocalDate.parse("2000-01-01"));

        // Create a list of users to be returned by the repository
        List<User> users = new ArrayList<>();

        users.add(new User(
                1L,
                "Andriy",
                "Moroz",
                "Andriy@example.com",
                LocalDate.parse("1995-05-20"),
                "123456789",
                "Kyivskaya 11"));

        users.add(new User(
                2L,
                "Sveta",
                "Savchenko",
                "Sveta@example.com",
                LocalDate.parse("1992-10-15"),
                "987654321",
                "Kyivskaya 15"));

        // Mock the repository's behavior to return the list of users
        when(userRepository.findAllByFilter(any(LocalDate.class), any(LocalDate.class), any(Sort.class)))
                .thenReturn(users);

        // Call the service method
        List<User> result = userService.getUsersByFilter(request);

        // Verify that the repository method was called with the correct parameters
        verify(userRepository).findAllByFilter(
                LocalDate.parse("1990-01-01"),
                LocalDate.parse("2000-01-01"),
                Sort.by(Sort.Order.asc("dateOfBirth"),
                        Sort.Order.asc("id")));

        // Assert that the result matches the expected list of users
        assertEquals(users, result);
    }

}
