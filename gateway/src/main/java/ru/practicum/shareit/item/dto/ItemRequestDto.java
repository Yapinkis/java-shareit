package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.comment.CommentRequestDto;

import java.util.List;

@Data
public class ItemRequestDto {
    @Size(max = 100)
    private String name;
    @Size(max = 255)
    private String description;
    private Boolean available;
    private Long requestId;
    private List<CommentRequestDto> comments;
}
