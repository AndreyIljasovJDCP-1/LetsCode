package com.example.letscode.controllers;

import com.example.letscode.models.Role;
import com.example.letscode.models.User;
import com.example.letscode.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Arrays.stream(Role.values()).toList());
        return "userEdit";
    }

    @PostMapping
    public String userSave(@RequestParam String username,
                           @RequestParam("roles") String[] roles,
                           @RequestParam("userId") User user) {
        user.setUsername(username);
        user.getRoles().clear();
        Arrays.stream(roles).forEach(role -> user.getRoles().add(Role.valueOf(role)));
        userRepository.save(user);

        return "redirect:/user";
    }
}