package ru.practicum.shareit.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private String name;
    private Long requestId;
    private LocalDateTime created = LocalDateTime.now();
    private List<ItemDto> items;
}
