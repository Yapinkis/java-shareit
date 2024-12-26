package ru.practicum.shareit.item.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    @JsonBackReference
    private ItemDto item;
    private UserDto user;
    private String authorName;
    private LocalDateTime created;
}
