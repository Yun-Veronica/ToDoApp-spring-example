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

//@Controller
//@RequestMapping("/tasks")
//public class TasksController {
//
//    @Autowired
//    private TasksService taskService;
//
////    @GetMapping
////    public String listTasks(Model model, Authentication authentication) {
////        Users user = (Users) authentication.getPrincipal();  // Get logged-in user
////        List<Tasks> tasks = taskService.getTasksForUser(user);
////        model.addAttribute("tasks", tasks);
////        return "tasks/list"; // Thymeleaf template for displaying tasks
////    }
//    @GetMapping
//    public String listTasks(Model model, Pageable pageable, Authentication authentication) {
//        Users user = (Users) authentication.getPrincipal();
//        Page<Tasks> tasksPage = taskService.getTasksForUser(user, pageable);
//        model.addAttribute("tasksPage", tasksPage);
//        return "tasks/list";  // Adjust template to display paginated tasks
//    }
//
//    @GetMapping("/create")
//    public String showCreateForm(Model model) {
//        model.addAttribute("task", new Tasks());
//        return "tasks/create"; // Thymeleaf template for creating a task
//    }
//
//    @PostMapping("/create")
//    public String createTask(@ModelAttribute Tasks task, Authentication authentication, Model model) {
//
//        Users user = (Users) authentication.getPrincipal();
//        task.setUser(user); // Set logged-in user
//        try {
//            // Call service to save the task
//            taskService.saveTask(task);
//            return "redirect:/tasks"; // Redirect to tasks list page
//
//        } catch (IllegalArgumentException e) {
//            // If the exception is thrown, pass the error message to the model
//            model.addAttribute("errorMessage", e.getMessage());
//            return "task-form"; // Return to the task creation form with an error
//        }
////        taskService.saveTask(task);
////        return "redirect:/tasks"; // Redirect back to task list
//    }
//
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable Long id, Model model) {
//        Tasks task = taskService.getTaskById(id);
//        model.addAttribute("task", task);
//        return "tasks/edit"; // Thymeleaf template for editing a task
//    }
//
//    @PostMapping("/edit/{id}")
//    public String updateTask(@PathVariable Long id, @ModelAttribute Tasks task) {
//        task.setId(id); // Ensure task ID is not overwritten
//        taskService.saveTask(task);
//        return "redirect:/tasks";
//    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteTask(@PathVariable Long id) {
//        taskService.deleteTask(id);
//        return "redirect:/tasks";
//    }
//
//}

//TODO
//user profile picture
// Add to them разбиенеи по папкам и возвратам templates

@Controller
@RequestMapping("/tasks")
public class TasksController {

    @Autowired
    private TasksService taskService;

    @Autowired
    private UserService userService;

    //    @GetMapping("/tasks")
//    public String listTasks(Model model, Pageable pageable, Authentication authentication) {
//        Users user = (Users) authentication.getPrincipal();
////        Page<Tasks> tasksPage = taskService.getTasksForUser(user, pageable);
////        model.addAttribute("tasksPage", tasksPage);
//        Page<Tasks> tasksPage = taskService.getTasks(pageable);
//        model.addAttribute("tasksPage", tasksPage);
//        return "tasks";  // Adjust template to display paginated tasks
//    }
    @GetMapping("/all")
    public String listTasks(Model model, @PageableDefault(size = 10) Pageable pageable) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username of the authenticated user
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        Page<Tasks> tasksPage;
        Users currentUser = userService.findByUsername(username);


        if (isAdmin) {
            // If the user is an admin, show all tasks
            tasksPage = taskService.getAllTasks(pageable);
        } else {
            // If the user is a regular user, show only their tasks
            tasksPage = taskService.getTasksByUser(currentUser, pageable);
        }

        model.addAttribute("tasksPage", tasksPage); // Add the tasks to the model

        return "tasks/tasks"; // Return to the list template
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

        Users user = (Users) authentication.getPrincipal();
        task.setUser(user); // Set logged-in user

        // Check if due_date is in the past
        if (task.getDue_date() != null && task.getDue_date().isBefore(LocalDate.now())) {
            model.addAttribute("errorMessage", "Due date cannot be in the past.");
            return "tasks/create";
        }

        try {
            taskService.saveTask(task);
            return "redirect:/tasks"; // Redirect to tasks list page
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "tasks/create"; // Return to the task creation form with error
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tasks task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "tasks/edit"; // Thymeleaf template for editing a task
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute @Valid Tasks task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/edit";  // Return to the edit form with validation errors
        }
        task.setId(id); // Ensure task ID is not overwritten
        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}
