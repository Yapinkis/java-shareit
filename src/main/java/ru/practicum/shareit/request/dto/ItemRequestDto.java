package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private Long requester;
    private LocalDateTime created;
    // Зачем на вообще нужен Request если мы его не используем?
    // Это тоже нужно для следующего ТЗ или роль Request у нас выполняет Booking, что очень похоже на правду
}
