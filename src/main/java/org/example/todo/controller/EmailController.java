package org.example.todo.controller;

import jakarta.mail.internet.InternetAddress;
import org.example.todo.model.EmailDetails;
import org.example.todo.model.Users;
import org.example.todo.service.EmailService;
import org.example.todo.service.EmailServiceImpl;
import org.example.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EmailController {

    @Value("${spring.mail.from}")
    private String msgFrom;

    @Autowired
    private EmailServiceImpl emailService;  // Using the interface

    @Autowired
    private UserService userService;

    // Get the list of users to send email to
    @GetMapping("/sendMail")
    public String showEmailForm(Model model) {
        List<Users> users = userService.allUsers(); // Fetch all users from the database
        model.addAttribute("users", users); // Add the list of users to the model
        return "mail/send_mail"; // Render the send email form page
    }

    // Handle the form submission to send the email
    @PostMapping("/sendMail")
    public String sendMail(@RequestParam("recipientEmail") String recipientEmail,
                           @RequestParam("subject") String subject,
                           @RequestParam("message") String message,
                           Model model) {

        // Create EmailDetails object with form data
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(recipientEmail);
        emailDetails.setSubject(subject);
        emailDetails.setMessage(message);
        emailDetails.setFrom(msgFrom);

        // Send the email using the EmailService
        String status = emailService.sendSimpleMail(emailDetails);

        // Add the status message to the model
        model.addAttribute("status", status);
        return "mail/send_mail"; // Return to the email form with the status
    }
}
