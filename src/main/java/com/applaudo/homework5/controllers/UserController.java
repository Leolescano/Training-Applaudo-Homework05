package com.applaudo.homework5.controllers;

import static com.applaudo.homework5.dto.UserResponseDTO.convertToDTO;

import com.applaudo.homework5.dto.UserResponseDTO;
import com.applaudo.homework5.entities.User;
import com.applaudo.homework5.services.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired private UserService userService;

  @GetMapping("/all")
  public ResponseEntity<Object> getAllUsers() {
    try {
      List<User> users = userService.getAllUsers();
      if (users.isEmpty()) {
        return ResponseEntity.badRequest().body("The User table is empty.");
      }
      return ResponseEntity.ok(users);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/get/{email}")
  public ResponseEntity<Object> getUserByEmail(@PathVariable String email) {
    try {
      Optional<User> userOptional = userService.getUserByEmail(email);
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        UserResponseDTO userDTO = convertToDTO(user);
        return ResponseEntity.ok(userDTO);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/create")
  public ResponseEntity<Object> createUser(@RequestBody User user) {
    try {
      userService.createUser(user);
      return ResponseEntity.ok(user);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/update/{userId}")
  public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody User user) {
    try {
      User userUpdate = userService.updateUser(userId, user);
      return ResponseEntity.ok(userUpdate);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/delete/{userId}")
  public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
    try {
      userService.deleteUser(userId);
      return ResponseEntity.ok("User id " + userId + " successfully deleted.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
