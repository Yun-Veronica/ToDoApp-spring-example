package org.example.todo.controller;

import org.example.todo.model.Users;
import org.example.todo.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;  // Directory to store uploaded pictures

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new Users());
        return "user/registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @ModelAttribute("userForm") @Valid Users userForm,
            BindingResult bindingResult,
            @RequestParam("profilePicture") MultipartFile profilePicture, // Receive the file directly
            Model model) {

        // If there are validation errors, return to the form
        if (bindingResult.hasErrors()) {
            return "user/registration";
        }

        // Passwords don't match
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "user/registration";
        }

        // Save the user if the username is unique
        if (!userService.saveUser(userForm)) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "user/registration";
        }



        long maxFileSize = 10 * 1024 * 1024; // 10MB limit
        if (profilePicture.getSize() > maxFileSize) {
            model.addAttribute("uploadError", "File is too large. Max size is 10MB.");
            return "user/registration";
        }

        String fileName = profilePicture.getOriginalFilename();
        Path path = Paths.get(uploadDir, fileName);

// Check if the file already exists
        int count = 1;
        while (Files.exists(path)) {
            String newFileName = fileName.substring(0, fileName.lastIndexOf('.')) + "_" + count++ + fileName.substring(fileName.lastIndexOf('.'));
            path = Paths.get(uploadDir, newFileName);
        }

        try {
            Files.copy(profilePicture.getInputStream(), path);
            userForm.setProfilePicturePath(path.getFileName().toString());
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("uploadError", "Error uploading the profile picture!");
            return "user/registration";
        }

        System.out.println("Saving file: " + path.toString());

        // After registration, redirect to home (or login)
        return "redirect:/";
    }
}