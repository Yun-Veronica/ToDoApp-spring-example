package org.example.todo.controller;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.example.todo.model.*;
import org.example.todo.repository.*;
import org.example.todo.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/tasks")
public class TasksController {

    @Autowired
    private TasksService taskService;

    @Autowired
    private UserService userService;


    @GetMapping("/all")
    public String listTasks(Model model,
                            @PageableDefault(size = 10) Pageable pageable,
                            @RequestParam(value = "search", required = false) String search) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username of the authenticated user
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        Page<Tasks> tasks;
        Users currentUser = userService.findByUsername(username);

        if (isAdmin) {
            // If the user is an admin, show all tasks, optionally filtered by search
            if (search != null && !search.isEmpty()) {
                tasks = taskService.searchTasks(search, pageable); // Add search filter
            } else {
                tasks = taskService.getAllTasks(pageable);
            }
        } else {
            // If the user is a regular user, show only their tasks, optionally filtered by search
            if (search != null && !search.isEmpty()) {
                tasks = taskService.searchTasksByUser(currentUser, search, pageable); // Add search filter for user tasks
            } else {
//                tasks = taskService.getTasksByUser(currentUser, pageable);
                tasks = taskService.getTasksByUser(currentUser, PageRequest.of(0, 10));
            }
        }

        // Ensure tasksPage is never null
        if (tasks == null) {
            tasks = Page.empty(); // Fallback to an empty page
        }

        System.out.println("Logged-in user: " + username);
        System.out.println("Retrieved tasks: " + tasks.getContent());


        model.addAttribute("tasksPage", tasks);
        model.addAttribute("search", search); // Add search term to the model to preserve it in the form
        model.addAttribute("tasks", tasks);

        return "tasks/all";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Tasks());
        return "tasks/create"; // Thymeleaf template for creating a task
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute @Valid Tasks task, BindingResult bindingResult, Authentication authentication, Model model) {

        if (bindingResult.hasErrors()) {
            return "tasks/create";  // Return to the create form with validation errors
        }
        authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) authentication.getPrincipal();
        System.out.println("User:" + user.getId());
        System.out.println("User:" + user.toString());
        if (user == null) {
            model.addAttribute("errorMessage", "Unable to identify the logged-in user.");
            return "tasks/create";
        }

        task.setUser(user); // Set logged-in user

        if (task.getDue_date() != null && task.getDue_date().isBefore(LocalDate.now())) {
            model.addAttribute("task", task);
            model.addAttribute("errorMessage", "Due date cannot be in the past.");
            return "tasks/create";
        }

        try {
            taskService.saveTask(task);
            return "redirect:/tasks/all"; // Redirect to tasks list page
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "tasks/create"; // Return to the task creation form with error
        }
    }


    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tasks task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "tasks/update"; // Thymeleaf template for editing a task
    }

//    @PostMapping("/update/{id}")
//    public String updateTask(@PathVariable Long id, @ModelAttribute @Valid Tasks task, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "tasks/update";  // Return to the edit form with validation errors
//        }
//        task.setId(id); // Ensure task ID is not overwritten
//        taskService.saveTask(task);
//        return "redirect:/tasks/all";
//    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute @Valid Tasks task, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "tasks/update";  // Return to the edit form with validation errors
        }

        Tasks existingTask = taskService.getTaskById(id); // Fetch the existing task
        if (existingTask == null) {
            throw new IllegalArgumentException("Task not found.");
        }

        // Preserve the user association
        Users user = existingTask.getUser();
        task.setUser(user);
        task.setId(id); // Ensure task ID is not overwritten

        taskService.saveTask(task);
        return "redirect:/tasks/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks/all";
    }
}

