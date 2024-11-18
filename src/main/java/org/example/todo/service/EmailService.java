package org.example.todo.service;

import org.example.todo.model.*;
import org.example.todo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//@Service
//public class EmailService {
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendNotification(String to, String subject, String message) {
//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setTo(to);
//        email.setSubject(subject);
//        email.setText(message);
//        mailSender.send(email);
//    }
//}
