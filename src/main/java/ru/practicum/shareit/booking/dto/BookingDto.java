package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
    private Item item;
    private User booker;
    private BookingStatus status = BookingStatus.WAITING;
}
