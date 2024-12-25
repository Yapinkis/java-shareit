package ru.practicum.shareit.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;
    @Column(name = "name", nullable = false, length = 255)
    private String name;
}
