package ru.practicum.shareit.utility;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Data
@Component
@AllArgsConstructor
public class Utility {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    public static final String TEXT = "text";

    public void checkUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ValidationException("Ошибка валидации, пользователь с данным email уже существует");
        }
    }


    public void checkItem(Item item, Long id) {
        if (Objects.equals(item.getOwner().getId(),(id))) {
            throw new ValidationException("Пользователь пытается забронировать свою собственную вещь");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Бронь для предмета недоступна");
        }
    }

    public void checkBooking(BookingDto bookingDto) {
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("Время начала бронирования не может совпадать со " +
                    "временем конца бронирования");
        }
    }

    public void checkBooking(Booking booking, Long userId) {
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new ValidationException("Id бронирования не совпадают "
                    + booking.getBooker().getId() + " != " + userId);
        }
    }


    public void checkBooking(List<Booking> booking) {
        if (Objects.equals(booking.size(),0)) {
            throw new ValidationException("Указан несуществующий владелец вещи");
        }
    }


    public void checkBooking(Booking booking, User user) {
        if (!Objects.equals(booking.getBooker().getId(), user.getId())) {
            throw new ValidationException("Пользователь " + user.getId() + " не брал данный предмет " + booking.getItem() + " в аренду");
        }
        if (booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Бронирование ещё не завершено, комментарии пока недоступны.");
        }
    }

    public BookingDto selectBooking(Booking booking, Long userId) {
        if (Objects.equals(booking.getBooker().getId(),userId)) {
            return BookingMapper.toBookingDto(booking);
        }
        if (Objects.equals(booking.getItem().getOwner().getId(),userId)) {
            return BookingMapper.toBookingDto(booking);
        }
        throw new ValidationException("Отказ в доступе, т.к. запрашивающий пользователь не является " +
                "Автором бронирования, либо владельцем вещи");
    }

    public List<Booking> chooseBookingByUser(Long id, String state) {
        return switch (state.toUpperCase()) {
            case "CURRENT" -> bookingRepository.findCurrentBookings(id, LocalDateTime.now());
            case "PAST" -> bookingRepository.findPastBookings(id, LocalDateTime.now());
            case "FUTURE" -> bookingRepository.findFutureBookings(id, LocalDateTime.now());
            case "WAITING" -> bookingRepository.findBookingsByStatus(id, BookingStatus.WAITING);
            case "REJECTED" -> bookingRepository.findBookingsByStatus(id, BookingStatus.REJECTED);
            default -> bookingRepository.findAllBookingsByUser(id);
        };
    }

    public List<Booking> chooseBookingByOwner(Long id, String state) {
        return switch (state.toUpperCase()) {
            case "CURRENT" -> bookingRepository.findCurrentBookingsForOwner(id, LocalDateTime.now());
            case "PAST" -> bookingRepository.findPastBookingsForOwner(id, LocalDateTime.now());
            case "FUTURE" -> bookingRepository.findFutureBookingsForOwner(id, LocalDateTime.now());
            case "WAITING" -> bookingRepository.findBookingsForOwnerByStatus(id, BookingStatus.WAITING);
            case "REJECTED" -> bookingRepository.findBookingsForOwnerByStatus(id, BookingStatus.REJECTED);
            default -> bookingRepository.findAllBookingsForOwner(id);
        };
    }

}
