package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

interface UserRepository {
    UserDto createUser(UserDto user);

    UserDto getUser(Long id);

    UserDto updateUser(UserDto user);

    void deleteUser(Long id);
}