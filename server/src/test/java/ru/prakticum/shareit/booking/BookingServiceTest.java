package ru.prakticum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utility.Utility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class BookingServiceTest {
    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Utility utility;

    private BookingDto bookingDto;
    private Booking booking;
    private Item item;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setOwner(user);

        booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(2));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusHours(2));
    }

    @Test
    void create_ShouldReturnCreatedBooking() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.create(bookingDto, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getItem().getId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void getBooking_ShouldReturnBooking_WhenFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(utility.selectBooking(booking, 1L)).thenReturn(BookingMapper.toBookingDto(booking));

        BookingDto result = bookingService.getBooking(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookingRepository, times(1)).findById(1L);
        verify(utility, times(1)).selectBooking(booking, 1L);
    }

    @Test
    void create_ShouldThrowException_WhenItemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookingService.create(bookingDto, 1L));

        assertEquals("Предмет с указанным Id1 не найден", exception.getMessage());
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void approve_ShouldChangeStatusToApproved() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(itemRepository.findByOwnerId(1L)).thenReturn(Optional.of(item));

        BookingDto result = bookingService.approve(1L, 1L, true);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void getUserBookings_ShouldReturnBookingList() {
        when(utility.chooseBookingByUser(1L, "ALL")).thenReturn(new ArrayList<>(List.of(booking)));
        List<BookingDto> result = bookingService.getUserBookings(1L, "ALL");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(utility, times(1)).chooseBookingByUser(1L, "ALL");
        verify(utility, times(1)).checkBooking(anyList());
    }

    @Test
    void getOwnerBookings_ShouldReturnBookingList() {
        when(utility.chooseBookingByOwner(1L, "ALL")).thenReturn(new ArrayList<>(List.of(booking)));

        List<BookingDto> result = bookingService.getOwnerBookings(1L, "ALL");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(utility, times(1)).chooseBookingByOwner(1L, "ALL");
        verify(utility, times(1)).checkBooking(anyList());
    }

    @Test
    void approve_ShouldChangeStatusToRejected() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(itemRepository.findByOwnerId(1L)).thenReturn(Optional.of(item));

        BookingDto result = bookingService.approve(1L, 1L, false);

        assertNotNull(result);
        assertEquals(BookingStatus.REJECTED, result.getStatus());

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void getUserBookings_ShouldReturnEmptyList_WhenNoBookings() {
        when(utility.chooseBookingByUser(1L, "ALL")).thenReturn(Collections.emptyList());

        List<BookingDto> result = bookingService.getUserBookings(1L, "ALL");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(utility, times(1)).chooseBookingByUser(1L, "ALL");
        verify(utility, times(1)).checkBooking(anyList());
    }

}
