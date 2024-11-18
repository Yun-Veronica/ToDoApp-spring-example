package org.example.todo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;



@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
public class Tasks {

    @Id
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "categories_id")
    private Categories category;


    @Column
    @NotEmpty(message = "Title is required")
    private String name;

    @Column
    private String task_text;

    @Column
    private String priority;

    @Column
    private LocalDate creation_date;

    @Column
    private LocalDate due_date;
}
