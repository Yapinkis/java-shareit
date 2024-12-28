package ru.prakticum.shareit.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.user.UserDto;


import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(classes = ShareItServer.class)
public class ItemDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<ItemDto> json;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void testSerialize() throws Exception {
        UserDto ownerDto = new UserDto();
        ownerDto.setId(1L);
        ownerDto.setName("OwnerName");
        ownerDto.setEmail("owner@gmail.com");

        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setName("UserName");
        userDto.setEmail("user@gmail.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Laptop");
        itemDto.setDescription("High-end laptop");
        itemDto.setAvailable(true);
        itemDto.setRequestId(10L);
        itemDto.setOwner(ownerDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
        commentDto.setAuthorName(userDto.getName());
        commentDto.setCreated(LocalDateTime.now());
        commentDto.setUser(userDto);
        commentDto.setItem(itemDto);

        itemDto.setComments(Arrays.asList(commentDto));

        JsonContent<ItemDto> jsonContent = json.write(itemDto);

        assertThat(jsonContent).hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.description")
                .hasJsonPath("$.available")
                .hasJsonPath("$.owner.id")
                .hasJsonPath("$.owner.name")
                .hasJsonPath("$.owner.email")
                .hasJsonPath("$.comments[0].text")
                .hasJsonPath("$.comments[0].authorName");

        assertThat(jsonContent)
                .extractingJsonPathNumberValue("$.owner.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(ownerDto.getId()));
        assertThat(jsonContent).extractingJsonPathStringValue("$.owner.name").isEqualTo(ownerDto.getName());
        assertThat(jsonContent).extractingJsonPathStringValue("$.owner.email").isEqualTo(ownerDto.getEmail());
        assertThat(jsonContent).extractingJsonPathStringValue("$.comments[0].text").isEqualTo(commentDto.getText());
        assertThat(jsonContent).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo(commentDto.getAuthorName());
    }
}
