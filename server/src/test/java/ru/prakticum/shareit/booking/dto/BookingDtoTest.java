package ru.prakticum.shareit.booking.dto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ShareItServer.class)
public class BookingDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<BookingDto> json;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void testSerialize() throws Exception {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusMonths(1);

        UserDto ownerDto = new UserDto();
        ownerDto.setId(1L);
        ownerDto.setName("OwnerName");
        ownerDto.setEmail("owner@gmail.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Laptop");
        itemDto.setDescription("High-end laptop");
        itemDto.setAvailable(true);
        itemDto.setOwner(ownerDto);
        itemDto.setRequestId(10L);
        itemDto.setLastBooking(null);
        itemDto.setNextBooking(null);

        UserDto bookerDto = new UserDto();
        bookerDto.setId(1L);
        bookerDto.setName("John");
        bookerDto.setEmail("john@gmail.com");

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(startDate);
        bookingDto.setEnd(endDate);
        bookingDto.setItemId(1L);
        bookingDto.setItem(itemDto);
        bookingDto.setBooker(bookerDto);
        bookingDto.setStatus(BookingStatus.WAITING);

        JsonContent<BookingDto> jsonContent = json.write(bookingDto);

        assertThat(jsonContent).hasJsonPath("$.id")
                .hasJsonPath("$.start")
                .hasJsonPath("$.end")
                .hasJsonPath("$.itemId")
                .hasJsonPath("$.item.id")
                .hasJsonPath("$.item.name")
                .hasJsonPath("$.item.description")
                .hasJsonPath("$.item.available")
                .hasJsonPath("$.item.owner.id")
                .hasJsonPath("$.item.owner.name")
                .hasJsonPath("$.item.owner.email")
                .hasJsonPath("$.booker.id")
                .hasJsonPath("$.booker.name")
                .hasJsonPath("$.booker.email")
                .hasJsonPath("$.status");

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id")
                .satisfies(bookingId -> assertThat(bookingId.longValue()).isEqualTo(bookingDto.getId()));

        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .satisfies(startDateStr -> assertThat(startDateStr.substring(0, 23)).isEqualTo(bookingDto.getStart().toString().substring(0, 23)));

        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .satisfies(endDateStr -> assertThat(endDateStr.substring(0, 23))
                        .isEqualTo(bookingDto.getEnd().toString().substring(0, 23)));

        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId")
                .satisfies(itemId -> assertThat(itemId.longValue()).isEqualTo(bookingDto.getItemId()));

        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.id")
                .satisfies(itemId -> assertThat(itemId.longValue()).isEqualTo(bookingDto.getItem().getId()));

        assertThat(jsonContent).extractingJsonPathStringValue("$.item.name")
                .satisfies(itemName -> assertThat(itemName).isEqualTo(bookingDto.getItem().getName()));

        assertThat(jsonContent).extractingJsonPathStringValue("$.item.description")
                .satisfies(itemDescription -> assertThat(itemDescription).isEqualTo(bookingDto.getItem().getDescription()));

        assertThat(jsonContent).extractingJsonPathBooleanValue("$.item.available")
                .satisfies(itemAvailable -> assertThat(itemAvailable).isEqualTo(bookingDto.getItem().getAvailable()));

        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.owner.id")
                .satisfies(ownerId -> assertThat(ownerId.longValue()).isEqualTo(bookingDto.getItem().getOwner().getId()));

        assertThat(jsonContent).extractingJsonPathStringValue("$.item.owner.name")
                .satisfies(ownerName -> assertThat(ownerName).isEqualTo(bookingDto.getItem().getOwner().getName()));

        assertThat(jsonContent).extractingJsonPathStringValue("$.item.owner.email")
                .satisfies(ownerEmail -> assertThat(ownerEmail).isEqualTo(bookingDto.getItem().getOwner().getEmail()));

        assertThat(jsonContent).extractingJsonPathNumberValue("$.booker.id")
                .satisfies(bookerId -> assertThat(bookerId.longValue()).isEqualTo(bookingDto.getBooker().getId()));

        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.name")
                .satisfies(bookerName -> assertThat(bookerName).isEqualTo(bookingDto.getBooker().getName()));

        assertThat(jsonContent).extractingJsonPathStringValue("$.booker.email")
                .satisfies(bookerEmail -> assertThat(bookerEmail).isEqualTo(bookingDto.getBooker().getEmail()));

        assertThat(jsonContent).extractingJsonPathStringValue("$.status")
                .satisfies(status -> assertThat(status).isEqualTo(bookingDto.getStatus().toString()));
    }
}
