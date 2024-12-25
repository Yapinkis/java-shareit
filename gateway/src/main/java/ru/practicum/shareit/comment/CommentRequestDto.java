package ru.practicum.shareit.comment;

import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class CommentRequestDto {
    private Long id;
    private String text;
    private Item item;
    private User user;
    private String authorName;
    private LocalDateTime created;
}
