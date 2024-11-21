package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

interface UserRepository {
    UserDto createUser(User user);

    UserDto getUser(Long id);

    UserDto updateUser(User user);

    void deleteUser(Long id);
}