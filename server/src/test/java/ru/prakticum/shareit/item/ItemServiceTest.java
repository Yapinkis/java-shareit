package ru.prakticum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utility.Utility;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class ItemServiceTest {
    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private Utility utility;

    private Item item;
    private User user;
    private ItemDto itemDto;
    private Comment comment;
    private CommentDto commentDto;

    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("John");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setItem(item);
        comment.setUser(user);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setUser(user);
        itemRequest.setDescription("Description");
        itemRequest.setCreated(LocalDateTime.now());
    }

    @Test
    void create_ShouldReturnCreatedItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.create(itemDto, 1L);

        assertNotNull(result);
        assertEquals("Test Item", result.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void create_ShouldCreateItemWithRequestId_WhenRequestFound() {
        itemDto.setRequestId(100L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ItemRequest foundRequest = new ItemRequest();
        foundRequest.setId(100L);
        foundRequest.setDescription("Sample request");
        foundRequest.setUser(user);
        when(itemRequestRepository.findById(100L)).thenReturn(Optional.of(foundRequest));

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.create(itemDto, 1L);

        assertNotNull(result);
        assertEquals("Test Item", result.getName());
        verify(itemRequestRepository, times(1)).findById(100L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void update_ShouldUpdateItemFields() {
        when(itemRepository.findByOwnerId(1L)).thenReturn(Optional.of(item));

        ItemDto updatedDto = new ItemDto();
        updatedDto.setName("Updated Name");
        updatedDto.setDescription("Updated Description");
        updatedDto.setAvailable(false);

        ItemDto result = itemService.update(updatedDto, 1L);

        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertFalse(result.getAvailable());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void update_ShouldThrowException_WhenItemNotFoundForUser() {
        when(itemRepository.findByOwnerId(1L)).thenReturn(Optional.empty());

        ItemDto updatedDto = new ItemDto();
        updatedDto.setName("Updated Name");

        assertThrows(EntityNotFoundException.class, () -> itemService.update(updatedDto, 1L));
        verify(itemRepository, times(1)).findByOwnerId(1L);
    }

    @Test
    void update_ShouldNotChangeFields_WhenNullFieldsProvided() {
        when(itemRepository.findByOwnerId(1L)).thenReturn(Optional.of(item));

        ItemDto partialDto = new ItemDto();

        itemService.update(partialDto, 1L);

        assertEquals("Test Item", item.getName());
        assertEquals("Description", item.getDescription());
        assertTrue(item.getAvailable());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void get_ShouldReturnItem() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        when(bookingRepository.getLastBooking(1L)).thenReturn(null);
        when(bookingRepository.getNextBooking(1L)).thenReturn(null);
        when(commentRepository.getComments(1L)).thenReturn(Collections.emptyList());

        ItemDto result = itemService.get(1L, 1L);

        assertNotNull(result);
        assertEquals("Test Item", result.getName());
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).getLastBooking(1L);
        verify(bookingRepository, times(1)).getNextBooking(1L);
        verify(commentRepository, times(1)).getComments(1L);
    }

    @Test
    void get_ShouldThrowException_WhenItemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.get(1L, 1L));
    }

    @Test
    void get_ShouldReturnItemWithBookingsAndComments() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Booking lastBooking = new Booking();
        lastBooking.setId(10L);
        Booking nextBooking = new Booking();
        nextBooking.setId(20L);

        when(bookingRepository.getLastBooking(1L)).thenReturn(lastBooking);
        when(bookingRepository.getNextBooking(1L)).thenReturn(nextBooking);

        Comment comment = new Comment();
        comment.setId(5L);
        comment.setText("Some comment");
        when(commentRepository.getComments(1L)).thenReturn(List.of(comment));

        doNothing().when(utility).selectUserForDTOModel(any(), any(), any(), eq(1L));

        ItemDto result = itemService.get(1L, 1L);
        assertNotNull(result);
        assertEquals(1, result.getComments().size());
    }

    @Test
    void addComment_ShouldReturnComment() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.getBookingFromUserAndItem(1L, 1L)).thenReturn(Optional.of(booking));

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setText("Test comment");
        savedComment.setItem(item);
        savedComment.setUser(user);
        savedComment.setCommentTime(LocalDateTime.now());
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentDto result = itemService.addComment(1L, 1L, commentDto);

        assertNotNull(result);
        assertEquals("Test comment", result.getText());
        assertEquals(user.getName(), result.getAuthorName());
        verify(itemRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).getBookingFromUserAndItem(1L, 1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addComment_ShouldThrowException_WhenItemNotFound() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Any comment text");

        assertThrows(EntityNotFoundException.class,
                () -> itemService.addComment(1L, 999L, commentDto));

        verify(itemRepository, times(1)).findById(999L);
        verifyNoMoreInteractions(itemRepository, bookingRepository, commentRepository);
    }

    @Test
    void searchItem_ShouldReturnEmptyList_WhenNoMatches() {
        when(itemRepository.search("nonexistent")).thenReturn(Collections.emptyList());

        List<ItemDto> result = itemService.searchItem("nonexistent");

        assertTrue(result.isEmpty());
        verify(itemRepository, times(1)).search("nonexistent");
    }

    @Test
    void searchItem_ShouldReturnListOfItems_WhenMatchesFound() {
        when(itemRepository.search("test")).thenReturn(List.of(item));

        List<ItemDto> result = itemService.searchItem("test");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getName());
        verify(itemRepository, times(1)).search("test");
    }

    @Test
    void getAllFromUser_ShouldReturnEmptyList_WhenNoItems() {
        when(itemRepository.getAllFromUser(1L)).thenReturn(Collections.emptyList());

        List<ItemDto> result = itemService.getAllFromUser(1L);

        assertTrue(result.isEmpty());
        verify(itemRepository, times(1)).getAllFromUser(1L);
    }

    @Test
    void getAllFromUser_ShouldReturnItemDto_WhenNoBookingsButHasComments() {
        when(itemRepository.getAllFromUser(1L)).thenReturn(List.of(item));
        item.setId(1L);

        when(bookingRepository.getAllLastBookings(List.of(1L))).thenReturn(Collections.emptyList());
        when(bookingRepository.getAllNextBookings(List.of(1L))).thenReturn(Collections.emptyList());

        Comment comment = new Comment();
        comment.setId(5L);
        comment.setText("Some comment");
        comment.setItem(item);
        when(commentRepository.getAllComments(List.of(1L))).thenReturn(List.of(comment));

        doNothing().when(utility).setBookingsForItemDto(any(ItemDto.class),
                anyMap(), anyMap(), anyMap());

        List<ItemDto> result = itemService.getAllFromUser(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(utility, times(1)).setBookingsForItemDto(any(), anyMap(), anyMap(), anyMap());
    }

}
