package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.user.UserDto;

import java.util.List;

@Data
public class ItemRequestDto {
    private String name;
    private Long id;
    private String description;
    private Boolean available;
    private UserDto owner;
    private Long requestId;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<Comment> comments;
}
