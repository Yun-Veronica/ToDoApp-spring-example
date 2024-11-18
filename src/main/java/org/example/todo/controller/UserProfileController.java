package org.example.todo.controller;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import jakarta.validation.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import org.example.todo.model.*;
import org.example.todo.repository.*;
import org.example.todo.service.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping
    public String viewProfile(Authentication authentication, Model model) {
        Users user = (Users) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user/view"; // Template to view user profile
    }

    @GetMapping("/edit")
    public String editProfile(Authentication authentication, Model model) {
        Users user = (Users) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user/edit"; // Template for editing user profile
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute Users user, Authentication authentication, Model model) {
        Users loggedInUser = (Users) authentication.getPrincipal();

        // Update email or profile info (password can be updated in a separate method)
        loggedInUser.setEmail(user.getEmail());
        loggedInUser.setUsername(user.getUsername());

        // Handle profile picture if it's provided (simplified for the example)
        if (user.getEmail() != null) {
            loggedInUser.setEmail(user.getEmail());
        }

        // Save updated user profile
        userRepository.save(loggedInUser);
        model.addAttribute("successMessage", "Profile updated successfully!");

        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(Authentication authentication, @RequestParam("newPassword") String newPassword, Model model) {
        Users loggedInUser = (Users) authentication.getPrincipal();
        loggedInUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));

        userRepository.save(loggedInUser);
        model.addAttribute("successMessage", "Password updated successfully!");

        return "redirect:/profile";
    }
}

