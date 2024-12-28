package ru.prakticum.shareit.user;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ShareItServer.class)
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUser_ShouldSaveToDatabase() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@gmail.com");

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser.getId());
        assertEquals("Test User", createdUser.getName());
        assertTrue(userRepository.existsById(createdUser.getId()));
    }

    @Test
    void getUser_ShouldRetrieveFromDatabase() {
        User user = new User();
        user.setName("Database User");
        user.setEmail("dbuser@gmail.com");
        userRepository.save(user);

        UserDto foundUser = userService.getUser(user.getId());

        assertEquals("Database User", foundUser.getName());
        assertEquals("dbuser@gmail.com", foundUser.getEmail());
    }

    @Test
    void updateUser_ShouldModifyDatabaseEntry() {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("old@gmail.com");
        userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setName("New Name");

        UserDto updatedUser = userService.updateUser(user.getId(), userDto);

        assertEquals("New Name", updatedUser.getName());
        assertEquals("old@gmail.com", updatedUser.getEmail());
    }

    @Test
    void deleteUser_ShouldRemoveFromDatabase() {
        User user = new User();
        user.setName("To Delete");
        user.setEmail("delete@gmail.com");
        userRepository.save(user);

        userService.deleteUser(user.getId());

        assertFalse(userRepository.existsById(user.getId()));
    }
}
