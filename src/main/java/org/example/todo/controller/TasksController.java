package org.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class TasksController {


    @GetMapping("/")
    public String home(@RequestParam(value = "user", required = false) String user, Model model) {
        model.addAttribute("username", user);
        return "home_page";

    }


    //
//    //@ResponseStatus
//
//    //CREATE
//    @PostMapping("/createTask")
//    public String createTask(){
//        return "mock";
//    }
//
//    @PostMapping("/createUser")
//    public String createUser(){
//        return "mock";
//    }
//
//    @PostMapping("/createCategory")
//    public String createCategory(){
//        return "mock";
//    }
//
//    @PostMapping("/Auth")
//    public String login(UsersRepository userRepository){
//        userRepository.
//        return "login";
//    }
//
//    @PostMapping("/Login")
//    public String login(Users user){
//        return "login";
//    }
//
//
//    @GetMapping("/Logout")
//    public String logout(){
//        return "xxx";
//    }
//    //READ
//    @GetMapping("/getTasks")
//    public String getTasksByUser(){
//        return "mock";
//    }
//
//
//    @GetMapping("/getTasksByDate")
//    public String getTasksByDate(){
//        return "mock";
//    }
//
//    @GetMapping("/getTasksByCategory")
//    public String getTasksByCategory(){
//        return "mock";
//    }
//
//    @GetMapping("/getTask")
//    public String getTask(){
//        return ;
//    }
//
//    @GetMapping("/getUser")
//    public String getUser(){
//        return "mock";
//    }
//
//    @GetMapping("/getCategory")
//    public String getCategory(){
//        return "mock";
//    }
//
//    //UPDATE
//    @PutMapping("/updateTask")
//    public String updateTask(){
//        return "mock";
//    }
//
//    @PutMapping("/updateUser")
//    public String updateUser(){
//        return "mock";
//    }
//
//    @PutMapping("/updateCategory")
//    public String updateCategory(){
//        return "mock";
//    }
//
//
//    //DELETE
//    @DeleteMapping("/deleteTask")
//    public String deleteTask(){
//        return "mock";
//    }
//
//    @DeleteMapping("/deleteUser")
//    public String deleteUser(){
//        return "mock";
//    }
//
//    @DeleteMapping("/deleteCategory")
//    public String deleteCategory(){
//        return "mock";
//    }


    //CRUD for tasks
    //crud for user
    //crud for categories

    //AUTH FOR User

}
