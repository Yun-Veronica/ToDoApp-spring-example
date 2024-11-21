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
/////////////////////////////////////////////////////last working version below
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
//    @GetMapping("/all")
//    public String listTasks(Model model, @PageableDefault(size = 10) Pageable pageable) {
//        // Get the current authenticated user
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName(); // Get the username of the authenticated user
//        boolean isAdmin = authentication.getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
//
//        Page<Tasks> tasksPage;
//        Users currentUser = userService.findByUsername(username);
//
//
//        if (isAdmin) {
//            // If the user is an admin, show all tasks
//            tasksPage = taskService.getAllTasks(pageable);
//        } else {
//            // If the user is a regular user, show only their tasks
//            tasksPage = taskService.getTasksByUser(currentUser, pageable);
//        }
//
//        model.addAttribute("tasksPage", tasksPage); // Add the tasks to the model
//
////        return "tasks/tasks"; // Return to the list template
//        return "tasks/all"; // Return to the list template
//    }


    //    121365
//    public String listTasks(Model model,
//                            @PageableDefault(size = 10) Pageable pageable,
//                            @RequestParam(value = "search", required = false) String search) {
//
//        // Get the current authenticated user
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName(); // Get the username of the authenticated user
//        boolean isAdmin = authentication.getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
//
//        Page<Tasks> tasksPage;
//        Users currentUser = userService.findByUsername(username);
//
//        // Check if user is admin or regular user and set tasks accordingly
//        if (isAdmin) {
//            // Admins can see all tasks, optionally filtered by search
//            if (search != null && !search.isEmpty()) {
//                tasksPage = taskService.searchTasks(search, pageable); // Add search filter
//            } else {
//                tasksPage = taskService.getAllTasks(pageable);
//            }
//        } else {
//            // Regular users can only see their own tasks, optionally filtered by search
//            if (search != null && !search.isEmpty()) {
//                tasksPage = taskService.searchTasksByUser(currentUser, search, pageable); // Add search filter for user tasks
//            } else {
//                tasksPage = taskService.getTasksByUser(currentUser, pageable);
//            }
//        }
//
//        // Add the tasksPage to the model
//        model.addAttribute("tasksPage", tasksPage);
//        model.addAttribute("search", search); // Add search term to preserve the search in the form
//
//        return "tasks/all"; // Return to the tasks listing template
//    }
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
                tasks = taskService.getTasksByUser(currentUser, pageable);
            }
        }

        // Ensure tasksPage is never null
        if (tasks == null) {
            tasks = Page.empty(); // Fallback to an empty page
        }

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


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tasks task = taskService.getTaskById(id);
        model.addAttribute("task", task);
//        return "tasks/edit"; // Thymeleaf template for editing a task
        return "tasks/update"; // Thymeleaf template for editing a task
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute @Valid Tasks task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/update";  // Return to the edit form with validation errors
        }
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
// TaskController.java
//@Controller
//@RequestMapping("/tasks")
//public class TasksController {
//
//    @Autowired
//    private TasksService taskService;
//
//    // List all tasks with pagination
//    @GetMapping("/all")
//    public String getAllTasks(Model model,
//                              @RequestParam(value = "page", defaultValue = "0") int page,
//                              @RequestParam(value = "search", defaultValue = "") String search) {
//        Page<Tasks> tasks = taskService.getAllTasks(int page, search);
//        model.addAttribute("tasks", tasks);
//        model.addAttribute("search", search);
//        return "tasks/all"; // Thymeleaf template
//    }
//
//    // Show create task page
//    @GetMapping("/create")
//    public String createTaskForm(Model model) {
//        model.addAttribute("task", new Tasks());
//        return "tasks/create";
//    }
//
//    // Save new task
//    @PostMapping("/create")
//    public String createTask(@ModelAttribute Tasks task) {
//        taskService.saveTask(task);
//        return "redirect:/tasks/all";
//    }
//
//    // Show edit task page
//    @GetMapping("/update/{id}")
//    public String editTaskForm(@PathVariable("id") Long id, Model model) {
//        Tasks task = taskService.getTaskById(id);
//        model.addAttribute("task", task);
//        return "tasks/update";
//    }
//
//    // Update task
//    @PostMapping("/update")
//    public String updateTask(@ModelAttribute Tasks task) {
//        taskService.updateTask(task);
//        return "redirect:/tasks/all";
//    }
//
//    // Delete task
//    @GetMapping("/delete/{id}")
//    public String deleteTask(@PathVariable("id") Long id) {
//        taskService.deleteTask(id);
//        return "redirect:/tasks/all";
//    }
//}
//
