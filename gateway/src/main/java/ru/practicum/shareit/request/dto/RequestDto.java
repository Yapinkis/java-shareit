package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestDto {
    private Long id;
    private String description;
//    private String name;
    private Long requestId;
    private LocalDateTime created = LocalDateTime.now();
    private List<ItemDto> items;
}
