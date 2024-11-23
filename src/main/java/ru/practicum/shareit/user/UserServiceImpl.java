package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utility.Utility;

@Service
@RequiredArgsConstructor

class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final Utility utilityValidator;

    @Override
    public UserDto createUser(UserDto user) {
        utilityValidator.checkUser(user);
        return repository.createUser(user);
    }

    @Override
    public UserDto getUser(Long id) {
        return repository.getUser(id);
    }

    @Override
    public UserDto updateUser(Long id, UserDto user) {
        utilityValidator.checkUser(user);
        user.setId(id);
        return repository.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteUser(id);
    }


}