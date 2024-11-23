package ru.practicum.shareit.user;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@Data
public class UserRepositoryImpl implements UserRepository {
    private Long id = 0L;
    private Map<Long, User> users = new HashMap<>();

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setId(id);
        users.put(id++,user);
        log.info("Создан пользователь ={}", user.getName());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUser(Long id) {
        return UserMapper.toUserDto(users.get(id));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User updatedUser = users.get(userDto.getId());
        Optional.ofNullable(userDto.getId()).ifPresent(updatedUser::setId);
        Optional.ofNullable(userDto.getName()).ifPresent(updatedUser::setName);
        Optional.ofNullable(userDto.getEmail()).ifPresent(updatedUser::setEmail);
        log.info("Пользовател c id ={} обвновлён в базе данных", updatedUser.getId());
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
        log.info("Пользовател c id ={} удалён из базы данных", id);
    }

}