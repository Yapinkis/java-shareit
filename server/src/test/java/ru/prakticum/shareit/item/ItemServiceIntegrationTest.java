package ru.prakticum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ShareItServer.class)
@Transactional
public class ItemServiceIntegrationTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user = userRepository.save(user);

        item = new Item();
        item.setName("Test Item");
        item.setDescription("A test item description");
        item.setAvailable(true);
        item.setOwner(user);
        item = itemRepository.save(item);
    }

    @Test
    void create_ShouldSaveItemToDatabase() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("New Item");
        itemDto.setDescription("A new item description");
        itemDto.setAvailable(true);

        ItemDto createdItem = itemService.create(itemDto, user.getId());

        assertNotNull(createdItem.getId());
        assertEquals("New Item", createdItem.getName());
        assertEquals("A new item description", createdItem.getDescription());

        Optional<Item> savedItem = itemRepository.findById(createdItem.getId());
        assertTrue(savedItem.isPresent());
    }

    @Test
    void addComment_ShouldSaveCommentToDatabase() {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(BookingStatus.APPROVED);
        booking = bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("This is a comment");

        CommentDto createdComment = itemService.addComment(user.getId(), item.getId(), commentDto);

        assertNotNull(createdComment.getId());
        assertEquals("This is a comment", createdComment.getText());

        Optional<Comment> savedComment = commentRepository.findById(createdComment.getId());
        assertTrue(savedComment.isPresent());
        assertEquals("This is a comment", savedComment.get().getText());
    }
}
