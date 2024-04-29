package com.example.clearsolution_taskusers.service;

import com.example.clearsolution_taskusers.dto.UserCreateRequest;
import com.example.clearsolution_taskusers.dto.UserFilterRequest;
import com.example.clearsolution_taskusers.dto.UserPartialUpdateRequest;
import com.example.clearsolution_taskusers.mapping.UserMapper;
import com.example.clearsolution_taskusers.model.User;
import com.example.clearsolution_taskusers.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service class for managing users.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    /**
     * Creates a new user.
     *
     * @param request data for creating the user
     * @return the created user
     */
    @Override
    public User createUser(UserCreateRequest request) {
        User user = userMapper.convertCreateRequestToEntity(request);
        user = userRepository.save(user);
        return user;
    }

    /**
     * Deletes the user with the specified identifier.
     *
     * @param id the identifier of the user to be deleted
     * @throws NoSuchElementException if the user with the specified identifier is not found
     */
    @Override
    public void deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("User no found with id: " + id);
        }
    }

    /**
     * Updates all fields an existing user.
     *
     * @param id      the ID of the user to update
     * @param request data for updating the user
     * @return the updated user
     * @throws NoSuchElementException if no user exists with the given ID
     */
    @Override
    public User updateUser(Long id, UserCreateRequest request) {

        return userRepository.findById(id).map(user -> {
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setAddress(request.getAddress());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setDateOfBirth(request.getDateOfBirth());

            return userRepository.save(user);
        }).orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    /**
     * Partially updates an existing user.
     *
     * @param id      the ID of the user to update
     * @param request data for updating the user
     * @return the updated user
     * @throws NoSuchElementException if no user exists with the given ID
     */
    @Override
    public User partialUpdateUser(Long id, UserPartialUpdateRequest request) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (request.getFirstName()!=null) {
                user.setFirstName(request.getFirstName());
            }

            if (request.getLastName()!=null) {
                user.setLastName(request.getLastName());
            }

            if (request.getEmail()!=null) {
                user.setEmail(request.getEmail());
            }

            if (request.getAddress()!=null) {
                user.setAddress(request.getAddress());
            }

            if (request.getPhoneNumber()!=null) {
                user.setPhoneNumber(request.getPhoneNumber());
            }

            if (request.getDateOfBirth()!=null) {
                user.setDateOfBirth(request.getDateOfBirth());
            }

            return userRepository.save(user);

        } else {
            throw new NoSuchElementException("User not found with id: " + id);
        }
    }

    /**
     * Finds a user by ID.
     *
     * @param id the ID of the user to find
     * @return the found user
     * @throws NoSuchElementException if no user exists with the given ID
     */
    @Override
    public User findUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new NoSuchElementException("User not found with id: " + id);
        }
    }

    /**
     * Retrieves a list of users based on the provided filter criteria.
     *
     * @param request the UserFilterRequest object containing filter criteria
     * @return a list of users matching the filter criteria
     */
    @Override
    public List<User> getUsersByFilter(UserFilterRequest request) {

        Sort sort = Sort.by(
                Sort.Order.asc("dateOfBirth"),
                Sort.Order.asc("id")
        );

        return userRepository.findAllByFilter(
                request.getDateOfBirthFrom(),
                request.getDateOfBirthTo(),
                sort
        );
    }

}
