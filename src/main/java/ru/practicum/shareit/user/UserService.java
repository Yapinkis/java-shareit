package ru.practicum.shareit.user;


import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

interface UserService {
    UserDto createUser(User user);

    UserDto getUser(Long id);

    UserDto updateUser(Long id, User user);

    void deleteUser(Long id);
}