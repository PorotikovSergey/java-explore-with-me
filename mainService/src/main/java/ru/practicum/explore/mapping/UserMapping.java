package ru.practicum.explore.mapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.UserDto;
import ru.practicum.explore.dto.NewUserRequest;
import ru.practicum.explore.dto.UserShortDto;
import ru.practicum.explore.model.User;
import ru.practicum.explore.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMapping {
    private final UserService userService;

    public List<UserDto> getUsersAdmin(List<Long> ids, Integer from, Integer size) {

        List<User> list = userService.getUsersAdmin(ids, from, size);

        if (list.isEmpty()) {
            return Collections.emptyList();
            //вот тут нужно оставить так,так как для тестов нужна проверка на пустой лист,а не ApiError если юзеров нет
        }

        return list.stream().map(UserMapping::fromUserToDto).collect(Collectors.toList());
    }

    public UserDto addUserAdmin(NewUserRequest newUserRequest) {
        User user = fromUserRequestToUser(newUserRequest);

        User backUser = userService.addUserAdmin(user);

        return fromUserToDto(backUser);
    }

    public void deleteUserAdmin(long userId) {
        userService.deleteUserAdmin(userId);
    }


    public static UserDto fromUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static UserShortDto fromUserToShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }

    public static User fromUserRequestToUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());
        return user;
    }
}

