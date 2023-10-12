package com.applaudo.homework5.services;

import static com.applaudo.homework5.utils.Validations.emailValidator;
import static com.applaudo.homework5.utils.Validations.phoneNumberValidator;

import com.applaudo.homework5.entities.User;
import com.applaudo.homework5.repositories.UserRepository;
import com.applaudo.homework5.services.exceptions.ServicesNotFoundException;
import com.applaudo.homework5.utils.exceptions.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  public void createUser(User user) throws ServicesNotFoundException, ValidationException {
    List<String> missingFields = new ArrayList<>();

    if (user.getEmail() == null) {
      missingFields.add("Email");
    }
    if (user.getFirstName() == null) {
      missingFields.add("First Name");
    }
    if (user.getLastName() == null) {
      missingFields.add("Last Name");
    }

    if (!missingFields.isEmpty()) {
      String missingFieldsMsg = String.join(", ", missingFields);
      throw new RuntimeException("The following fields are required: " + missingFieldsMsg);
    }

    if (!emailValidator(user.getEmail())) {
      throw new ValidationException("The email address is not in a valid format.");
    }

    if (!phoneNumberValidator(user.getPhone())) {
      throw new ValidationException(
          "The phone number does not have the correct pattern (+503 #### ####).");
    }

    if (user.getFirstName().isEmpty() || user.getLastName().isEmpty()) {
      throw new ValidationException("First or last name cannot be blank.");
    }

    Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
    if (existingUser.isPresent()) {
      throw new ServicesNotFoundException("The user already exists with the given email account.");
    }
    userRepository.save(user);
  }

  public User updateUser(Long userId, User user) throws ServicesNotFoundException {
    Optional<User> existingUser = userRepository.findById(userId);
    if (existingUser.isEmpty()) {
      throw new ServicesNotFoundException("User not found");
    }

    User updatedUser = existingUser.get();

    if (user.getEmail() != null) {
      throw new ServicesNotFoundException("Cannot update a user's email");
    }

    if (user.getFirstName() != null) {
      updatedUser.setFirstName(user.getFirstName());
    }

    if (user.getLastName() != null) {
      updatedUser.setLastName(user.getLastName());
    }

    if (user.getPhone() != null) {
      updatedUser.setPhone(user.getPhone());
    }

    return userRepository.save(updatedUser);
  }

  public Optional<User> getUserByEmail(String email) throws ServicesNotFoundException {
    Optional<User> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      throw new ServicesNotFoundException("We could not find a user with the given email.");
    }
    return user;
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public void deleteUser(Long userId) throws ServicesNotFoundException {
    Optional<User> existingUser = userRepository.findById(userId);
    if (existingUser.isEmpty()) {
      throw new ServicesNotFoundException("User not found");
    }
    userRepository.deleteById(userId);
  }
}
