package com.example.letscode.controllers;

import com.example.letscode.models.Role;
import com.example.letscode.models.User;
import com.example.letscode.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.TreeSet;

@Controller
public class RegistrationController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(User user, Model model) {
        var userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb.isPresent()) {
            model.addAttribute("message", "User already exists!");
            return "registration";
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new TreeSet<>(List.of(Role.USER)));
        userRepository.save(user);
        return "redirect:/login";
    }
}
