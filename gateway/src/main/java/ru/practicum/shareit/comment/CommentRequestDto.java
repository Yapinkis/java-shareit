package ru.practicum.shareit.comment;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentRequestDto {
    private Long id;
    private String text;
    @NotBlank
    @Size(max = 500)
    private String description;
    @FutureOrPresent
    private LocalDateTime created;
}
