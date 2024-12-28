package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.comment.CommentRequestDto;

import java.util.List;

@Data
public class ItemRequestDto {
    @NotBlank
    @NotNull
    @Size(max = 100)
    private String name;
    @NotBlank
    @NotNull
    @Size(max = 255)
    private String description;
    @NotNull
    private Boolean available;
    private Long requestId;
    private List<CommentRequestDto> comments;
}
