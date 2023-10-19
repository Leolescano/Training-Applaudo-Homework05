package com.applaudo.homework5.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.applaudo.homework5.entities.User;
import com.applaudo.homework5.repositories.UserRepository;
import com.applaudo.homework5.services.exceptions.ServicesNotFoundException;
import com.applaudo.homework5.utils.exceptions.ValidationException;
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

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @InjectMocks private UserService userService;

  @Mock private UserRepository userRepository;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void shouldFailToCreateUserDueToMissingFields() {

    User user = new User(null, null, null, null, "+503 1234 5678", null);
    assertThrows(RuntimeException.class, () -> userService.createUser(user));
  }

  @Test
  public void shouldFailToCreateUserDueToInvalidEmail() {

    User user = new User(null, "Carlos", "Tejada", "invalid_email", "+503 1234 5678", null);
    assertThrows(ValidationException.class, () -> userService.createUser(user));
  }

  @Test
  public void shouldFailToCreateUserDueToInvalidPhoneNumber() {

    User user = new User(null, "Carlos", "Tejada", "carlostejada@gmail.com", "invalid_phone", null);
    assertThrows(ValidationException.class, () -> userService.createUser(user));
  }

  @Test
  public void shouldThrowExceptionWhenCreatingUserWithEmptyFirstName() {

    User user = new User(null, "", "Tejada", "carlostejada@gmail.com", "+503 1234 5678", null);
    assertThrows(ValidationException.class, () -> userService.createUser(user));
  }

  @Test
  public void shouldThrowExceptionWhenCreatingUserThatAlreadyExists() {

    User existingUser = new User(1L, "Carlos", "Tejada", "carlostejada@gmail.com", "+503 1234 5678", null);
    when(userRepository.findByEmail("carlostejada@gmail.com")).thenReturn(Optional.of(existingUser));

    User newUser = new User(null, "Carlos", "Tejada", "carlostejada@gmail.com", "+503 1234 5678", null);
    assertThrows(ServicesNotFoundException.class, () -> userService.createUser(newUser));
  }

  @Test
  public void shouldCreateUserSuccessfully() throws ValidationException, ServicesNotFoundException {

    User user = new User(null, "Carlos", "Tejada", "carlostejada@gmail.com", "+503 1234 5678", null);
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    userService.createUser(user);

    verify(userRepository, times(1)).save(user);
  }

  @Test
  public void shouldThrowExceptionWhenCreatingUserWithInvalidEmail() {

    User user = new User(null, "Carlos", "Tejada", "invalid-email", "+503 1234 5678", null);

    assertThrows(
        ValidationException.class,
        () -> {
          userService.createUser(user);
        });
  }

  @Test
  public void shouldThrowExceptionWhenCreatingUserWithExistingEmail() {

    User user = new User(null, "Carlos", "Tejada", "carlostejada@gmail.com", "+503 1234 5678", null);
    when(userRepository.findByEmail("carlostejada@gmail.com")).thenReturn(Optional.of(user));

    assertThrows(
        ServicesNotFoundException.class,
        () -> {
          userService.createUser(user);
        });
  }

  @Test
  public void shouldReturnAllUsers() {

    User user1 = new User(1L, "Carlos", "Tejada", "carlostejada@gmail.com ", "+503 1234 5678", null);
    User user2 = new User(2L, "Leonardo", "Lescano", "jane.doe@example.com", "+503 9876 5432", null);

    when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

    List<User> result = userService.getAllUsers();

    assertEquals(2, result.size());
    assertEquals(user1, result.get(0));
    assertEquals(user2, result.get(1));
  }

  @Test
  void shouldThrowExceptionWhenUpdatingNonexistentUser() {

    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(ServicesNotFoundException.class, () -> userService.updateUser(1L, new User()));

    verify(userRepository, never()).save(any());
  }

  @Test
  void shouldThrowExceptionWhenAttemptingToUpdateUserEmail() {

    User existingUser = new User();
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));

    User newUser = new User();
    newUser.setEmail("new.email@example.com");

    assertThrows(ServicesNotFoundException.class, () -> userService.updateUser(1L, newUser));

    verify(userRepository, never()).save(any());
  }

  @Test
  void shouldUpdateUserSuccessfully() throws ServicesNotFoundException {

    User existingUser = new User();
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));

    User newUser = new User();
    newUser.setFirstName("NewName");

    userService.updateUser(1L, newUser);

    assertEquals("NewName", existingUser.getFirstName());
    verify(userRepository).save(existingUser);
  }

  @Test
  void shouldThrowExceptionWhenFetchingUserByEmailNotFound() {

    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    assertThrows(
        ServicesNotFoundException.class, () -> userService.getUserByEmail("notfound@gmail.com"));

    verify(userRepository, never()).save(any());
  }

  @Test
  void shouldReturnUserByEmailSuccessfully() throws ServicesNotFoundException {
    User user = new User();
    user.setEmail("found@gmail.com");
    when(userRepository.findByEmail("found@gmail.com")).thenReturn(Optional.of(user));

    Optional<User> result = userService.getUserByEmail("found@gmail.com");

    assertTrue(result.isPresent());
    assertEquals("found@gmail.com", result.get().getEmail());
  }

  @Test
  public void shouldDeleteUserSuccessfullyWhenUserExists() throws ServicesNotFoundException {

    Long userId = 1L;
    User user = new User();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    userService.deleteUser(userId);

    verify(userRepository, times(1)).deleteById(userId);
  }

  @Test
  public void shouldThrowServicesNotFoundExceptionWhenDeletingNonexistentUser() {

    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(ServicesNotFoundException.class, () -> userService.deleteUser(userId));

    verify(userRepository, never()).deleteById(any());
  }
}
