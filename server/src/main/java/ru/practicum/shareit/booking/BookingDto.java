package ru.practicum.shareit.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private Long itemId;
    @FutureOrPresent @NotNull
    private LocalDateTime start;
    @FutureOrPresent @NotNull
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status = BookingStatus.WAITING;
}
