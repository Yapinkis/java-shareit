package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.utility.Utility;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid BookItemRequestDto bookItemRequestDto,
										 @RequestHeader(Utility.X_SHARER_USER_ID) Long id) {
		return bookingClient.create(id, bookItemRequestDto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Object> approve(@PathVariable Long id,
										  @RequestParam Boolean approved,
										  @RequestHeader(Utility.X_SHARER_USER_ID) Long userId) {
		return bookingClient.approve(id, approved, userId);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getBooking(@PathVariable Long id,
											 @RequestHeader(Utility.X_SHARER_USER_ID) Long userId) {
		return bookingClient.getBooking(id,userId);
	}

	@GetMapping
	public ResponseEntity<Object> getUserBookings(@RequestHeader(Utility.X_SHARER_USER_ID) Long id,
											@RequestParam(defaultValue = "ALL") String state,
											@RequestParam(defaultValue = "0") Integer from,
											@RequestParam(defaultValue = "10") Integer size) {
		return bookingClient.getUserBookings(id, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnerBookings(@RequestHeader(Utility.X_SHARER_USER_ID) Long owner,
											 @RequestParam(defaultValue = "ALL") String state,
											 @RequestParam(defaultValue = "0") Integer from,
											 @RequestParam(defaultValue = "10") Integer size) {
		return bookingClient.getOwnerBookings(owner, state, from, size);
	}

}
