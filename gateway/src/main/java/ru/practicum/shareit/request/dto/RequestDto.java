package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    @NotBlank
    @Size(max = 500)
    private String description;
    @FutureOrPresent
    private LocalDateTime created;
}
