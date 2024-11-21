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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TasksService {
    @Autowired
    private TasksRepository taskRepository;

    public List<Tasks> getTasksForUser(Users user) {
        return taskRepository.findByUser(user);
    }

    public Page<Tasks> getTasksByUser(Users user, Pageable pageable) {
        return taskRepository.findByUser(user, pageable);
    }

    public Page<Tasks> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Transactional
    public Tasks saveTask(Tasks task) {
        if (task.getDue_date() != null && task.getDue_date().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past.");
        }

        return taskRepository.save(task);
    }

    public Tasks getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid task ID"));
    }

    public List<Tasks> getAllTasks() {
        List<Tasks> All_tasks = taskRepository.findAll();
        return All_tasks;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Page<Tasks> searchTasks(String search, Pageable pageable) {
        return taskRepository.findByNameContainingIgnoreCase(search, pageable);
    }

    public Page<Tasks> searchTasksByUser(Users user, String search, Pageable pageable) {
        return taskRepository.findByUserAndNameContainingIgnoreCase(user, search, pageable);
    }


}
