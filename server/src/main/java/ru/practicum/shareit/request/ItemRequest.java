package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description",  nullable = false, length = 500)
    private String description;
    @ManyToOne
    @JoinColumn(name = "requester")
    private User user;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}