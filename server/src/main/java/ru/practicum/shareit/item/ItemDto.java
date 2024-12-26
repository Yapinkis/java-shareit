package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.user.UserDto;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemDto {
    @NotBlank
    @NotNull
    private String name;
    private Long id;
    @NotBlank
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private UserDto owner;
    private Long requestId;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    @JsonManagedReference
    private List<CommentDto> comments;
}
