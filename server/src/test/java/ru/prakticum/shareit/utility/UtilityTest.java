package ru.prakticum.shareit.utility;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utility.Utility;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UtilityTest {
    @InjectMocks
    private Utility utility;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    private UserDto userDto;
    private ItemDto itemDto;

    private User user;
    private Item item;
    private Booking lastBooking;
    private Booking nextBooking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");

        item = new Item();
        item.setId(1L);
        item.setOwner(user);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setOwner(userDto);

        lastBooking = new Booking();
        lastBooking.setId(10L);

        nextBooking = new Booking();
        nextBooking.setId(20L);
    }

    @Test
    void checkUser_ShouldThrowValidationException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> utility.checkUser(userDto));
        assertEquals("Ошибка валидации, пользователь с данным email уже существует", ex.getMessage());

        verify(userRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    void checkUser_ShouldNotThrowException_WhenEmailNotExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        assertDoesNotThrow(() -> utility.checkUser(userDto));
        verify(userRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    void selectUserForDTOModel_ShouldNotSetBookings_WhenOwnerIsNotUserId() {
        Long userId = 2L;
        itemDto.setOwner(userDto);
        utility.selectUserForDTOModel(nextBooking, lastBooking, itemDto, userId);

        assertNull(itemDto.getLastBooking(), "lastBooking should be null");
        assertNull(itemDto.getNextBooking(), "nextBooking should be null");
    }

    @Test
    void setBookingsForItemDto_ShouldSetNullBookingsAndEmptyComments_WhenMapsAreEmpty() {
        utility.setBookingsForItemDto(itemDto,
                Collections.emptyMap(),
                Collections.emptyMap(),
                Collections.emptyMap());

        assertNull(itemDto.getLastBooking(), "lastBooking should be null");
        assertNull(itemDto.getNextBooking(), "nextBooking should be null");
        assertNotNull(itemDto.getComments(), "comments list should not be null");
        assertTrue(itemDto.getComments().isEmpty(), "comments list should be empty");
    }

    @Test
    void chooseBookingByOwner_SingleTestForAllStates() {
        List<Booking> currentBookings = List.of(new Booking());
        List<Booking> pastBookings = List.of(new Booking(), new Booking());
        List<Booking> futureBookings = Collections.emptyList();
        List<Booking> waitingBookings = List.of(new Booking());
        List<Booking> rejectedBookings = List.of(new Booking());
        List<Booking> allBookings = List.of(new Booking(), new Booking(), new Booking());

        when(bookingRepository.findCurrentBookingsForOwner(eq(1L), any(LocalDateTime.class)))
                .thenReturn(currentBookings);
        when(bookingRepository.findPastBookingsForOwner(eq(1L), any(LocalDateTime.class)))
                .thenReturn(pastBookings);
        when(bookingRepository.findFutureBookingsForOwner(eq(1L), any(LocalDateTime.class)))
                .thenReturn(futureBookings);
        when(bookingRepository.findBookingsForOwnerByStatus(1L, BookingStatus.WAITING))
                .thenReturn(waitingBookings);
        when(bookingRepository.findBookingsForOwnerByStatus(1L, BookingStatus.REJECTED))
                .thenReturn(rejectedBookings);
        when(bookingRepository.findAllBookingsForOwner(1L))
                .thenReturn(allBookings);

        List<Booking> resultCurrent = utility.chooseBookingByOwner(1L, "CURRENT");
        List<Booking> resultPast = utility.chooseBookingByOwner(1L, "PAST");
        List<Booking> resultFuture = utility.chooseBookingByOwner(1L, "FUTURE");
        List<Booking> resultWaiting = utility.chooseBookingByOwner(1L, "WAITING");
        List<Booking> resultRejected = utility.chooseBookingByOwner(1L, "REJECTED");
        List<Booking> resultDefault = utility.chooseBookingByOwner(1L, "SOMETHING_UNKNOWN");

        assertEquals(currentBookings, resultCurrent, "CURRENT bookings must match");
        assertEquals(pastBookings, resultPast, "PAST bookings must match");
        assertEquals(futureBookings, resultFuture, "FUTURE bookings must match");
        assertEquals(waitingBookings, resultWaiting, "WAITING bookings must match");
        assertEquals(rejectedBookings, resultRejected, "REJECTED bookings must match");
        assertEquals(allBookings, resultDefault, "DEFAULT (all) bookings must match");

        verify(bookingRepository, times(1))
                .findCurrentBookingsForOwner(eq(1L), any(LocalDateTime.class));
        verify(bookingRepository, times(1))
                .findPastBookingsForOwner(eq(1L), any(LocalDateTime.class));
        verify(bookingRepository, times(1))
                .findFutureBookingsForOwner(eq(1L), any(LocalDateTime.class));
        verify(bookingRepository, times(1))
                .findBookingsForOwnerByStatus(1L, BookingStatus.WAITING);
        verify(bookingRepository, times(1))
                .findBookingsForOwnerByStatus(1L, BookingStatus.REJECTED);
        verify(bookingRepository, times(1))
                .findAllBookingsForOwner(1L);

        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void chooseBookingByUser_SingleTestForAllStates() {
        List<Booking> currentBookings = List.of(new Booking());
        List<Booking> pastBookings = List.of(new Booking(), new Booking());
        List<Booking> futureBookings = Collections.emptyList();
        List<Booking> waitingBookings = List.of(new Booking());
        List<Booking> rejectedBookings = List.of(new Booking());
        List<Booking> allBookings = List.of(new Booking(), new Booking(), new Booking());

        when(bookingRepository.findCurrentBookings(eq(1L), any(LocalDateTime.class)))
                .thenReturn(currentBookings);
        when(bookingRepository.findPastBookings(eq(1L), any(LocalDateTime.class)))
                .thenReturn(pastBookings);
        when(bookingRepository.findFutureBookings(eq(1L), any(LocalDateTime.class)))
                .thenReturn(futureBookings);
        when(bookingRepository.findBookingsByStatus(1L, BookingStatus.WAITING))
                .thenReturn(waitingBookings);
        when(bookingRepository.findBookingsByStatus(1L, BookingStatus.REJECTED))
                .thenReturn(rejectedBookings);
        when(bookingRepository.findAllBookingsByUser(1L))
                .thenReturn(allBookings);

        List<Booking> resultCurrent = utility.chooseBookingByUser(1L, "CURRENT");
        List<Booking> resultPast = utility.chooseBookingByUser(1L, "PAST");
        List<Booking> resultFuture = utility.chooseBookingByUser(1L, "FUTURE");
        List<Booking> resultWaiting = utility.chooseBookingByUser(1L, "WAITING");
        List<Booking> resultRejected = utility.chooseBookingByUser(1L, "REJECTED");
        List<Booking> resultDefault = utility.chooseBookingByUser(1L, "UNKNOWN_STATE");

        assertEquals(currentBookings, resultCurrent);
        assertEquals(pastBookings, resultPast);
        assertEquals(futureBookings, resultFuture);
        assertEquals(waitingBookings, resultWaiting);
        assertEquals(rejectedBookings, resultRejected);
        assertEquals(allBookings, resultDefault);

        verify(bookingRepository, times(1)).findCurrentBookings(eq(1L), any(LocalDateTime.class));
        verify(bookingRepository, times(1)).findPastBookings(eq(1L), any(LocalDateTime.class));
        verify(bookingRepository, times(1)).findFutureBookings(eq(1L), any(LocalDateTime.class));
        verify(bookingRepository, times(1)).findBookingsByStatus(1L, BookingStatus.WAITING);
        verify(bookingRepository, times(1)).findBookingsByStatus(1L, BookingStatus.REJECTED);
        verify(bookingRepository, times(1)).findAllBookingsByUser(1L);

        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void selectBooking_whenUserIsNeitherOwnerNorBooker_throwValidationException() {
        User booker = new User();
        booker.setId(300L);

        User owner = new User();
        owner.setId(400L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);

        Long someOtherId = 999L;
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> utility.selectBooking(booking, someOtherId)
        );

        assertTrue(ex.getMessage().contains("Отказ в доступе"), "В сообщении должно быть упоминание об отказе в доступе");
    }

}
