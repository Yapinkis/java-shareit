package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.dto.UserDto;

interface UserRepository extends JpaRepository<UserDto, Long> {
    UserDto createUser(UserDto user);

    UserDto getUser(Long id);

    UserDto updateUser(UserDto user);

    void deleteUser(Long id);
}