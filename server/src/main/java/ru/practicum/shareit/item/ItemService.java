package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utility.Utility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final Utility utility;

    public ItemDto create(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + userId + " не найден"));
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new EntityNotFoundException("Запрос с ID " + itemDto.getRequestId() + " не найден"));
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        item.setRequest(itemRequest);
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    public ItemDto update(ItemDto itemDto, Long userId) {
        Item item = itemRepository.findByOwnerId(userId).orElseThrow(()
                -> new EntityNotFoundException("Предмет = " + itemDto.getName() + " не найден"));
        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);
        Optional.ofNullable(itemDto.getOwner()).map(UserMapper::toUser).ifPresent(item::setOwner);
        itemRepository.save(item);
        log.info("Предмет с Id ={} обновлён", item.getId());
        return ItemMapper.toItemDto(item);
    }

    public ItemDto get(Long id, Long userId) {
        Item item = itemRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Предмет с Id = " + id + " не обнаружен"));
        final Long itemId = item.getId();
        Booking lastBooking = bookingRepository.getLastBooking(itemId);
        Booking nextBooking = bookingRepository.getNextBooking(itemId);
        List<Comment> comments = commentRepository.getComments(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        utility.selectUserForDTOModel(nextBooking,lastBooking,itemDto,userId);
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        itemDto.setComments(commentDtos);
        return itemDto;
    }

    public List<ItemDto> searchItem(String search) {
        List<Item> items = itemRepository.search(search);
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public List<ItemDto> getAllFromUser(Long id) {
        List<Item> items = itemRepository.getAllFromUser(id);
        List<Long> itemsIds = items.stream().map(Item::getId).toList();
        Map<Long,Booking> lastBookings = bookingRepository.getAllLastBookings(itemsIds).stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(), Function.identity(),(b1,b2) -> b1));
        Map<Long,Booking> nextBookings = bookingRepository.getAllNextBookings(itemsIds).stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(),Function.identity(),(b1,b2) -> b1));
        Map<Long, List<Comment>> comments = commentRepository.getAllComments(itemsIds).stream().collect(Collectors.groupingBy(comment -> comment.getItem().getId()));
        return items.stream().map(item -> {
                    ItemDto itemDto = ItemMapper.toItemDto(item);
                    utility.setBookingsForItemDto(itemDto, lastBookings, nextBookings, comments);
                    return itemDto;
                }).collect(Collectors.toList());
    }

    public CommentDto addComment(Long userId, Long id, CommentDto commentDto) {
        Item item = itemRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Предмет с Id = " + id + " не обнаружен"));
        User user = userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Пользователь c " + userId + " не обнаружен"));
        Booking booking = bookingRepository.getBookingFromUserAndItem(userId,id).orElseThrow(()
                -> new ValidationException("Бронирование пользователя с Id = " + userId + " не обнаружено"));
        utility.checkBooking(booking,user);
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setUser(user);
        comment.setCommentTime(LocalDateTime.now());
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

}
