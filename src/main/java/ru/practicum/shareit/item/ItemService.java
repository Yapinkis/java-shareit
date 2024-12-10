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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utility.Utility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final Utility utility;

    public ItemDto create(ItemDto itemDto, Long userId) {
        itemDto.setOwner(userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Пользователь c " + userId + " не обнаружен")));
        Item item = ItemMapper.toItem(itemDto);
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
        //Могу ли я вернуть просто itemDto приходящий в метод, что бы не выполнять лишние операции?
    }

    public ItemDto update(ItemDto itemDto, Long userId) {
        Item item = itemRepository.findByOwnerId(userId).orElseThrow(()
                -> new EntityNotFoundException("Предмет с Id = " + itemDto.getId() + " не найден"));
        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);
        Optional.ofNullable(itemDto.getOwner()).ifPresent(item::setOwner);
        Optional.ofNullable(itemDto.getRequest()).ifPresent(item::setRequest);
        itemRepository.save(item);
        log.info("Предмет с Id ={} обновлён", item.getId());
        return ItemMapper.toItemDto(item);
    }

    public ItemDto get(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Предмет с Id = " + id + " не обнаружен"));
/*      Booking lastBooking = bookingRepository.getMaxBooking();
        ItemDto itemDto = ItemMapper.toItemDto(item);
        itemDto.setLastBooking(lastBooking);
        return itemDto;

        Здесь очень странная история по тестам в Postman. С одной стороны они требуют наличие
        полей lastBooking и nextBooking у DTO модели, но при этом их значение должно быть null


        pm.test("Test item 'lastBooking' field", function () {
        var jsonData = pm.response.json();
        pm.expect(jsonData).to.have.property('lastBooking');
        pm.expect(jsonData.lastBooking, '"lastBooking" must be "null"').null;
    });
        pm.test("Test item 'nextBooking' field", function () {
        var jsonData = pm.response.json();
        pm.expect(jsonData).to.have.property('nextBooking');
        pm.expect(jsonData.nextBooking, '"nextBooking" must be "null"').null;
    });
        Я так понимаю, эти поля нужны для следующего ТЗ?
 */
        ItemDto itemDto = ItemMapper.toItemDto(item);
        itemDto.setComments(commentRepository.getComments(item.getId()));
        return itemDto;
    }

    public List<ItemDto> searchItem(String search) {
        List<Item> items = itemRepository.search(search);
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public List<ItemDto> getAllFromUser(Long id) {
        List<Item> items = itemRepository.getAllFromUser(id);
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public CommentDto addComment(Long userId, Long id, CommentDto commentDto) {
        Item item = itemRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Предмет с Id = " + id + " не обнаружен"));
        User user = userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Пользователь c " + userId + " не обнаружен"));
        Booking booking = bookingRepository.getBookingFromUserAndItem(userId,id).orElseThrow(()
                -> new ValidationException("Бронирование пользователя с Id = " + userId + " не обнаружено"));;
        utility.checkBooking(booking,user);
        commentDto.setItem(item);
        commentDto.setUser(user);
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.toComment(commentDto);
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

}
