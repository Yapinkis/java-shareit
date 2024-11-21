package ru.practicum.shareit.user;


import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@Data
public class UserRepositoryImpl implements UserRepository {
    private Long id = 0L;
    private Map<Long, User> users = new HashMap<>();

    @Override
    public UserDto createUser(User user) {
        User newUser = new User();
        newUser.setId(id);
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        users.put(id++,newUser);
        return UserMapper.toUserDTO(newUser);
    }

    @Override
    public UserDto getUser(Long id) {
        return UserMapper.toUserDTO(users.get(id));
    }

    @Override
    public UserDto updateUser(User user) {
        User updatedUser = users.get(user.getId());
        Optional.ofNullable(user.getId()).ifPresent(updatedUser::setId);
        Optional.ofNullable(user.getName()).ifPresent(updatedUser::setName);
        Optional.ofNullable(user.getEmail()).ifPresent(updatedUser::setEmail);
        return UserMapper.toUserDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

}