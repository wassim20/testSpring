package com.elokencia.springApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private Long id;
    private String name;
    private String email;


    @OneToMany(mappedBy = "assignee")
    private List<Task> tasks;
}