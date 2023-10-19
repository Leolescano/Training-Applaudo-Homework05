package com.applaudo.homework5.config;

import com.applaudo.homework5.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
  @Autowired private UserRepository userRepository;

  @Override
  public void run(String... args) throws Exception {

    //    User u1 = new User(null, "Maria", "Brown", "maria@gmail.com", "988888888", null);
    //    User u2 = new User(null, "Alex", "Green", "alex@gmail.com", "977777777", null);
    //    User u3 = new User(null, "Leonardo", "Lescano", "leolescanomdq@gmail.com", "47997635498",
    // null);
    //    userRepository.saveAll(Arrays.asList(u1, u2));
    //    userRepository.save(u3);
  }
}
