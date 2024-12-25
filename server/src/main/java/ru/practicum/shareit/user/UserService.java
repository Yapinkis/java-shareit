package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.utility.Utility;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Utility utility;

    public UserDto createUser(UserDto userDto) {
        utility.checkUser(userDto);
        User user = UserMapper.toUser(userDto);
        user = userRepository.save(user);
        log.info("Создан пользователь с ID = {}, имя = {}", user.getId(), user.getName());
        return UserMapper.toUserDto(user);
    }

    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь c " + id + " не обнаружен"));
        return UserMapper.toUserDto(user);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        utility.checkUser(userDto);
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь c " + id + " не обнаружен"));
        Optional.ofNullable(userDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userDto.getEmail()).ifPresent(user::setEmail);
        userRepository.save(user);
        log.info("Пользователь с Id ={} обновлён", user.getId());
        return UserMapper.toUserDto(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("Пользователь с Id ={} удалён", id);
    }


}