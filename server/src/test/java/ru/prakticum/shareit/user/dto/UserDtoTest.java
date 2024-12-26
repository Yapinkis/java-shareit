package ru.prakticum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.UserDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(classes = ShareItServer.class)
public class UserDtoTest {
    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<UserDto> json;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void testSerialize() throws Exception {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setName("Test User");

        JsonContent<UserDto> jsonContent = json.write(userDto);

        assertThat(jsonContent).hasJsonPath("$.id")
                .hasJsonPath("$.email")
                .hasJsonPath("$.name");

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(userDto.getId().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
    }
}
