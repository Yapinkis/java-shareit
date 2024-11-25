package ru.practicum.shareit.item.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;
    private String text;
    private Long itemId;
    private Long authorId;
    private LocalDateTime commentTime;
    //Нужно ли нам делать DTO для этой сущности?
}
