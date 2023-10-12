package com.applaudo.homework5.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(length = 50, nullable = false)
  private String firstName;

  @NotBlank
  @Column(length = 50, nullable = false)
  private String lastName;

  @Email
  @Column(unique = true, nullable = false)
  private String email;

  @Pattern(regexp = "\\+503 [0-9]{4} [0-9]{4}", message = "Invalid phone number")
  private String phone;
}
