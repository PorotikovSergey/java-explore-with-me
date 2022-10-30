package ru.practicum.explore.responses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.dto.UserDto;
import ru.practicum.explore.dto.NewUserRequest;
import ru.practicum.explore.model.User;
import ru.practicum.explore.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserResponse {
    private final UserService userService;
    private final Mapper mapper;

    public ResponseEntity<Object> getUsersAdmin(List<Long> ids, Integer from, Integer size) {

        List<User> list = userService.getUsersAdmin(ids, from, size);

        if (list.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
            //вот тут нужно оставить так,так как для тестов нужна проверка на пустой лист,а не ApiError если юзеров нет
        }

        List<UserDto> resultList = list.stream().map(mapper::fromUserToDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> addUserAdmin(NewUserRequest newUserRequest) {
        User user = mapper.fromUserRequestToUser(newUserRequest);

        User backUser = userService.addUserAdmin(user);

        UserDto userDto = mapper.fromUserToDto(backUser);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    public void deleteUserAdmin(long userId) {
        userService.deleteUserAdmin(userId);
    }
}

