package com.applaudo.homework5.dto;

import com.applaudo.homework5.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
  private String firstName;
  private String lastName;
  private String phone;

  public static UserResponseDTO convertToDTO(User user) {
    UserResponseDTO dto = new UserResponseDTO();
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.setPhone(user.getPhone());
    return dto;
  }
}
