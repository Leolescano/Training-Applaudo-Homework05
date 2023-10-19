package com.applaudo.homework5.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.applaudo.homework5.entities.User;
import com.applaudo.homework5.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  private MockMvc mockMvc;

  @InjectMocks private UserController userController;

  @Mock private UserService userService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  public void shouldReturnAllUsersSuccessfully() throws Exception {

    List<User> users =
        Arrays.asList(
            new User(null, "Carlos", "Tejada", "carlostejada@gmail.com", "+503 1234 5678", null),
            new User(null, "Leonardo", "Lescano", "leolescanomdq@gmail.com", "+503 9876 5432", null));

    when(userService.getAllUsers()).thenReturn(users);

    mockMvc
        .perform(get("/users/all").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].firstName").value("Carlos"))
        .andExpect(jsonPath("$[1].firstName").value("Leonardo"));
  }

  @Test
  public void shouldReturnEmptyListWhenNoUsersExist() throws Exception {

    when(userService.getAllUsers()).thenReturn(new ArrayList<>());

    mockMvc
        .perform(get("/users/all").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("The User table is empty."));
  }

  @Test
  public void shouldHandleErrorWhenFetchingAllUsers() throws Exception {

    when(userService.getAllUsers()).thenThrow(new RuntimeException("Error retrieving users"));

    mockMvc
        .perform(get("/users/all").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Error retrieving users"));
  }

  @Test
  public void shouldHandleErrorWhenFetchingUserByEmail() throws Exception {

    String email = "carlostejada@gmail.com";

    when(userService.getUserByEmail(email))
        .thenThrow(new RuntimeException("User retrieval failed"));

    mockMvc
        .perform(get("/users/get/" + email).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("User retrieval failed"));
  }

  @Test
  public void shouldReturnNotFoundWhenEmailNotInDatabase() throws Exception {

    String email = "carlostejada@gmail.com";

    when(userService.getUserByEmail(email)).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/users/get/" + email).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldCreateUserSuccessfully() throws Exception {

    User user = new User(null, "Carlos", "Tejada", "carlostejada@gmail.com", "+503 1234 5678", null);

    doNothing().when(userService).createUser(any(User.class));

    mockMvc
        .perform(
            post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName", is("Carlos")));
  }

  @Test
  public void shouldNotCreateUserDueToError() throws Exception {

    User user = new User(null, "Carlos", "Tejada", "carlostejada@gmail.com", "+503 1234 5678", null);

    doThrow(new RuntimeException("Creation failed")).when(userService).createUser(any(User.class));

    mockMvc
        .perform(
            post("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Creation failed"));
  }

  @Test
  void shouldUpdateUserSuccessfully() throws Exception {

    User mockUser = new User();
    mockUser.setId(1L);
    mockUser.setFirstName("John");
    mockUser.setLastName("Doe");
    mockUser.setPhone("+503 1234 5678");

    when(userService.updateUser(anyLong(), any(User.class))).thenReturn(mockUser);

    ResponseEntity<Object> responseEntity = userController.updateUser(1L, mockUser);

    assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    assertThat(responseEntity.getBody()).isEqualTo(mockUser);
  }

  @Test
  void shouldFailToUpdateUser() throws Exception {

    User mockUser = new User();
    mockUser.setId(1L);
    mockUser.setFirstName("Carlos");
    mockUser.setLastName("Tejada");
    mockUser.setPhone("+503 1234 5678");

    when(userService.updateUser(anyLong(), any(User.class)))
        .thenThrow(new RuntimeException("Some error"));

    ResponseEntity<Object> responseEntity = userController.updateUser(1L, mockUser);

    assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    assertThat(responseEntity.getBody()).isEqualTo("Some error");
  }

  @Test
  public void shouldSuccessfullyDeleteUser() throws Exception {
    doNothing().when(userService).deleteUser(1L);

    mockMvc.perform(delete("/users/delete/1")).andExpect(status().isOk());

    verify(userService, times(1)).deleteUser(1L);
  }

  @Test
  public void shouldFailToDeleteUser() throws Exception {
    Long userId = 1L;

    // Simulamos que el servicio arroja una excepción.
    doThrow(new RuntimeException("Deletion failed")).when(userService).deleteUser(userId);

    mockMvc
        .perform(delete("/users/delete/" + userId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Deletion failed"));
  }
}
