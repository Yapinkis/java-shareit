package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.utility.Utility;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestBody BookingDto bookingDto, @RequestHeader(Utility.X_SHARER_USER_ID) Long id) {
        return bookingService.create(bookingDto, id);
        //я убираю аннотацию @Valid т.к. её по идее теперь выполняет gateway?
        //и ещё,я на вход контроллеру принимаю ту же модел BookItemRequestDto что и в gateway, которая имеет поля с проверкой валидации
        //а врзвращаю свою модель bookingDto с необходимыми по условиям полями, верен ли подобный подход?
        // Или я могу дополнить модель указанную в gateway просто дополнить её необходимыми полями?
     }

    @PatchMapping("/{id}")
    public BookingDto approve(@PathVariable Long id,
                              @RequestParam Boolean approved,
                              @RequestHeader(Utility.X_SHARER_USER_ID) Long userId) {
        return bookingService.approve(id, userId, approved);
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable Long id, @RequestHeader(Utility.X_SHARER_USER_ID) Long userId) {
        return bookingService.getBooking(id,userId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestHeader(Utility.X_SHARER_USER_ID) Long id,
                                            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getUserBookings(id, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader(Utility.X_SHARER_USER_ID) Long owner,
                                             @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getOwnerBookings(owner, state);
    }

}
