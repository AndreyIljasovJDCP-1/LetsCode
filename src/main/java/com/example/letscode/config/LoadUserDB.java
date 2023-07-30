package com.example.letscode.config;

import com.example.letscode.models.Role;
import com.example.letscode.models.User;
import com.example.letscode.repositories.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

@Log
@Configuration
public class LoadUserDB {

    @Bean
    @ConditionalOnProperty(name = "productTable.data.preload", havingValue = "true")
    CommandLineRunner initDatabase(UserRepository repository,PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("1"));
        //user.setPassword(NoOpPasswordEncoder.getInstance().encode("1"));
        //user.setPassword("1");
        user.setActive(true);
        user.setRoles(new TreeSet<>(List.of(Role.USER)));
        return args ->
                log.info("Preloading " + repository.save(user));

    }
}
