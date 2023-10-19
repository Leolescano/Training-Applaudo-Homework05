package com.applaudo.homework5.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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

  // @NotBlank
  @Column(length = 50, nullable = false)
  private String firstName;

  // @NotBlank
  @Column(length = 50, nullable = false)
  private String lastName;

  // @Email
  @Column(unique = true, nullable = false)
  private String email;

  // @Pattern(regexp = "\\+503 [0-9]{4} [0-9]{4}", message = "Invalid phone number")
  private String phone;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
}
