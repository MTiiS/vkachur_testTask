package com.example.clearsolution_taskusers.rest;

import com.example.clearsolution_taskusers.dto.*;
import com.example.clearsolution_taskusers.model.User;
import com.example.clearsolution_taskusers.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UsersController.class)
@PropertySource("classpath:validationValues.properties")
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testFindUserById() throws Exception {
        // Create a sample user
        User user = new User();
        user.setId(1L);
        user.setFirstName("Andriy");
        user.setLastName("Moroz");
        user.setEmail("Andriy.Moroz@example.com");
        user.setDateOfBirth(LocalDate.of(1990, 5, 15));

        // Mock the userService to return the sample user when finding by ID
        when(userService.findUserById(1L)).thenReturn(user);

        // Perform a GET request to retrieve the user by ID and assert the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value("Andriy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value("Moroz"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("Andriy.Moroz@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.dateOfBirth").value("1990-05-15"));
    }

    @Test
    public void testCreateUser() throws Exception {
        // Prepare a user creation request
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstName("Andriy");
        request.setLastName("Moroz");
        request.setEmail("Andriy.Moroz@example.com");
        request.setDateOfBirth(LocalDate.of(1990, 5, 15));

        // Prepare a sample user
        User user = new User();
        user.setId(1L);
        user.setFirstName("Andriy");
        user.setLastName("Moroz");
        user.setEmail("Andriy.Moroz@example.com");
        user.setDateOfBirth(LocalDate.of(1990, 5, 15));

        // Mock the userService to return the sample user when creating a user
        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(user);

        // Perform a POST request to create a user and assert the response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"Andriy\", \"lastName\": \"Moroz\", \"email\": \"Andriy.Moroz@example.com\", \"dateOfBirth\": \"1990-05-15\" }"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/users/1"));
    }

    @Test
    void createUser_InvalidRequest_BlankFirstName_ReturnsBadRequest() throws Exception {
        // Perform a POST request with an invalid request (blank first name) and expect BadRequest response
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"test@example.com\", \"firstName\": \"\", \"lastName\": \"Moroz\", \"dateOfBirth\": \"2002-04-29\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0].status").value("400"));
    }

    @Test
    void createUser_InvalidRequest_BlankLastName_ReturnsBadRequest() throws Exception {
        // Perform a POST request with an invalid request (blank last name) and expect BadRequest response
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Andriy\", \"lastName\": \"\",\"email\": \"test@example.com\",   \"dateOfBirth\": \"2002-04-29\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0].status").value("400"));
    }

    @Test
    void createUser_InvalidRequest_InvalidEmail_ReturnsBadRequest() throws Exception {
        // Perform a POST request with an invalid request (invalid email) and expect BadRequest response
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"Andriy\",  \"lastName\": \"Moroz\", \"email\": \"invalidemail\",  \"dateOfBirth\": \"2002-04-29\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0].status").value("400"));
    }

    @Test
    void createUser_InvalidRequest_NullEmail_ReturnsBadRequest() throws Exception {
        // Perform a POST request with an invalid request (null email) and expect BadRequest response
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"Andriy\", \"lastName\": \"Moroz\", \"dateOfBirth\": \"2002-04-29\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0].status").value("400"));
    }

    @Test
    void createUser_InvalidRequest_MinAge_ReturnsBadRequest() throws Exception {
        // Perform a POST request with an invalid request (minimum age violation) and expect BadRequest response
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"Andriy\", \"lastName\": \"Moroz\", \"email\": \"invalidemail\", \"dateOfBirth\": \"2010-04-29\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0].status").value("400"));
    }

    @Test
    void createUser_InvalidRequest_NullDateOfBirth_ReturnsBadRequest() throws Exception {
        // Perform a POST request with an invalid request (null date of birth) and expect BadRequest response
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"Andriy\", \"lastName\": \"Moroz\", \"email\": \"invalidemail\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0].status").value("400"));
    }

    @Test
    void updateUser_ValidRequest_ReturnsOk() throws Exception {
        // Prepare a user creation request
        LocalDate dateOfBirth = LocalDate.now().minusYears(25);
        UserCreateRequest request = new UserCreateRequest(
                "NewAndriy",
                "NewMoroz",
                "newtest@example.com",
                dateOfBirth,
                null,
                null);

        // Perform a PUT request to update a user and expect OK response
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{  \"firstName\": \"NewAndriy\", \"lastName\": \"NewMoroz\", \"email\": \"newtest@example.com\",  \"dateOfBirth\": \"" + dateOfBirth + "\" }"))
                .andExpect(status().isOk());

        // Verify that the updateUser method was called with the correct parameters
        verify(userService, times(1)).updateUser(eq(1L), eq(request));
    }

    @Test
    void partialUpdateUser_ValidRequest_ReturnsOk() throws Exception {
        // Prepare a partial update request
        UserPartialUpdateRequest request = new UserPartialUpdateRequest();
        request.setEmail("newtest@example.com");

        // Perform a PATCH request to partially update a user and expect OK response
        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"newtest@example.com\" }"))
                .andExpect(status().isOk());

        // Verify that the partialUpdateUser method was called with the correct parameters
        verify(userService, times(1)).partialUpdateUser(eq(1L), eq(request));
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() throws Exception {
        // Prepare a list of sample users
        List<User> users = new ArrayList<>();
        users.add(new User(
                1L,
                "Vlad",
                "Simpson",
                "Vlad@example.com",
                LocalDate.of(1990, 5, 15),
                "123456789",
                "Bereznyakivska 12"));

        users.add(new User(
                2L,
                "Dima",
                "Smith",
                "Dima@example.com",
                LocalDate.of(2000, 5, 15),
                "987654321",
                "Bereznyakivska 31"));

        // Mock the userService to return the list of users
        when(userService.getUsersByFilter(any(UserFilterRequest.class))).thenReturn(users);

        // Perform a GET request to retrieve all users and assert the response
        mockMvc.perform(get("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].firstName").value("Vlad"))
                .andExpect(jsonPath("$.data[0].lastName").value("Simpson"))
                .andExpect(jsonPath("$.data[0].email").value("Vlad@example.com"))
                .andExpect(jsonPath("$.data[0].dateOfBirth").exists())
                .andExpect(jsonPath("$.data[0].phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.data[0].address").value("Bereznyakivska 12"))
                .andExpect(jsonPath("$.data[1].firstName").value("Dima"))
                .andExpect(jsonPath("$.data[1].lastName").value("Smith"))
                .andExpect(jsonPath("$.data[1].email").value("Dima@example.com"))
                .andExpect(jsonPath("$.data[1].dateOfBirth").exists())
                .andExpect(jsonPath("$.data[1].phoneNumber").value("987654321"))
                .andExpect(jsonPath("$.data[1].address").value("Bereznyakivska 31"));
    }

    @Test
    void getAllUsersByFilter_ReturnsListOfUsers() throws Exception {
        // Prepare a list of sample users
        List<User> users = new ArrayList<>();
        users.add(new User(1L,
                "Vlad",
                "Simpson",
                "Vlad@example.com",
                LocalDate.of(1994, 5, 15),
                "123456789",
                "Bereznyakivska 12"));

        users.add(new User(2L,
                "Dima",
                "Smith",
                "Dima@example.com",
                LocalDate.of(2000, 5, 15),
                "987654321",
                "Bereznyakivska 31"));

        // Mock the userService to return the list of users
        when(userService.getUsersByFilter(any(UserFilterRequest.class))).thenReturn(users);

        // Perform a GET request to retrieve users by filter and assert the response
        mockMvc.perform(get("/api/users/")
                        .param("dateOfBirthFrom", "1990-01-01")
                        .param("dateOfBirthTo", "2001-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].firstName").value("Vlad"))
                .andExpect(jsonPath("$.data[0].lastName").value("Simpson"))
                .andExpect(jsonPath("$.data[0].email").value("Vlad@example.com"))
                .andExpect(jsonPath("$.data[0].dateOfBirth").exists())
                .andExpect(jsonPath("$.data[0].phoneNumber").value("123456789"))
                .andExpect(jsonPath("$.data[0].address").value("Bereznyakivska 12"))
                .andExpect(jsonPath("$.data[1].firstName").value("Dima"))
                .andExpect(jsonPath("$.data[1].lastName").value("Smith"))
                .andExpect(jsonPath("$.data[1].email").value("Dima@example.com"))
                .andExpect(jsonPath("$.data[1].dateOfBirth").exists())
                .andExpect(jsonPath("$.data[1].phoneNumber").value("987654321"))
                .andExpect(jsonPath("$.data[1].address").value("Bereznyakivska 31"));
    }

    @Test
    void getAllUsers_ByInvalidFilter_ReturnsBadRequest() throws Exception {
        // Prepare a list of sample users
        List<User> users = new ArrayList<>();

        users.add(new User(1L,
                "Vlad",
                "Simpson",
                "Vlad@example.com",
                LocalDate.now().minusYears(30),
                "123456789",
                "Bereznyakivska 12"));

        users.add(new User(2L,
                "Dima",
                "Smith",
                "Dima@example.com",
                LocalDate.now().minusYears(25),
                "987654321",
                "Bereznyakivska 31"));

        // Mock the userService to return the list of users
        when(userService.getUsersByFilter(any(UserFilterRequest.class))).thenReturn(users);

        // Perform a GET request with invalid filter parameters and expect BadRequest response
        mockMvc.perform(get("/api/users/")
                        .param("dateOfBirthFrom", "2055-01-01")
                        .param("dateOfBirthTo", "2000-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0].status").value("400"));
    }

    @Test
    void deleteUser_ValidRequest_ReturnsOk() throws Exception {
        // Perform a DELETE request to delete a user and expect OK response
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        // Verify that the deleteUser method was called with the correct parameter
        verify(userService, times(1)).deleteUser(eq(1L));
    }

    @Test
    void deleteUser_UserNotFound_ReturnsNotFound() throws Exception {
        // Set the ID of the user to delete
        Long userId = 1L;

        // Mock the behavior of the deleteUser method to throw NoSuchElementException
        doThrow(new NoSuchElementException("User not found with id: " + userId))
                .when(userService).deleteUser(userId);

        // Perform a request to delete the user and expect a 404 Not Found status
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].status").value("404"));

        // Verify that the deleteUser method was called with the correct ID
        verify(userService, times(1)).deleteUser(eq(userId));
    }

}
