package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.user.UserDto;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemDto {
    private String name;
    private Long id;
    private String description;
    private Boolean available;
    private UserDto owner;
    private Long requestId;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
}
