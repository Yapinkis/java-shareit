package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utility.Utility;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final Utility utility;

    public BookingDto create(BookingDto bookingDto, Long id) {
        utility.checkBooking(bookingDto);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(()
                -> new EntityNotFoundException("Предмет с указанным Id" + bookingDto.getItemId() + " не найден"));
        utility.checkItem(item,id);
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь c " + id +
                " не обнаружен"));
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);
        bookingRepository.save(booking);
        log.info("Заказ с Id ={} для позиции ={} в базу данных", booking.getId(), booking.getItem().getName());
        return BookingMapper.toBookingDto(booking);
    }

    public BookingDto approve(Long id, Long userId, boolean available) {
        itemRepository.existsById(itemRepository.findByOwnerId(userId).orElseThrow(()
                        -> new ValidationException("Предмет с пользователя указанным Id " + userId + " не найден"))
                .getId());
        Booking booking = bookingRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Бронирование с указанным Id" + id + " не обнаружено"));
        utility.checkBooking(booking,userId);
        if (Objects.equals(available,true)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()
                -> new EntityNotFoundException("Бронирование с указанным Id" + userId + " не обнаружено"));
        return utility.selectBooking(booking,userId);
    }

    public List<BookingDto> getUserBookings(Long id, String state) {
        List<Booking> bookings = utility.chooseBookingByUser(id, state);
        utility.checkBooking(bookings);
        bookings.sort((b1, b2) -> b2.getStart().compareTo(b1.getStart()));
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    public List<BookingDto> getOwnerBookings(Long id, String state) {
        List<Booking> bookings = utility.chooseBookingByOwner(id,state);
        utility.checkBooking(bookings);
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

}
