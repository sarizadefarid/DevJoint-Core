package com.example.week1.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category { // Mütləq public olmalıdır!
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // Boş constructor
    public Category() {}

    // Getter və Setter-lər
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}