package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text",nullable = false, length = 1000)
    private String text;
    @ManyToOne
    @JoinColumn(name = "item")
    private Item itemId;
    @ManyToOne
    @JoinColumn(name = "author")
    private User authorId;
    @Column(name = "comment_time",nullable = false)
    private LocalDateTime commentTime;
    //Нужно ли нам делать DTO для этой сущности?
}
