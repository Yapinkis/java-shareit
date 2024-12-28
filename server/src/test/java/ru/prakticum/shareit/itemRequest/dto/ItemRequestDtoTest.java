package ru.prakticum.shareit.itemRequest.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.request.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.Collections;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = ShareItServer.class)
public class ItemRequestDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<ItemRequestDto> json;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void testSerialize() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Test Request");
        itemRequestDto.setName("Request Name");
        itemRequestDto.setRequestId(10L);
        itemRequestDto.setCreated(created);
        itemRequestDto.setItems(Collections.singletonList(itemDto));

        JsonContent<ItemRequestDto> jsonContent = json.write(itemRequestDto);

        assertThat(jsonContent).hasJsonPath("$.id")
                .hasJsonPath("$.description")
                .hasJsonPath("$.name")
                .hasJsonPath("$.requestId")
                .hasJsonPath("$.created")
                .hasJsonPath("$.items[0].id")
                .hasJsonPath("$.items[0].name")
                .hasJsonPath("$.items[0].description")
                .hasJsonPath("$.items[0].available");

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestDto.getId().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDto.getDescription());
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo(itemRequestDto.getName());
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemRequestDto.getRequestId().intValue());
        assertThat(jsonContent).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(itemDto.getId().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("$.items[0].name").isEqualTo(itemDto.getName());
        assertThat(jsonContent).extractingJsonPathStringValue("$.items[0].description").isEqualTo(itemDto.getDescription());
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(itemDto.getAvailable());
    }
}
