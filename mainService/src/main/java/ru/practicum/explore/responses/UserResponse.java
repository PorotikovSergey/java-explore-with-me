package ru.practicum.explore.responses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.apierrors.ForbiddenError;
import ru.practicum.explore.apierrors.NotFoundApiError;
import ru.practicum.explore.dto.UserDto;
import ru.practicum.explore.dto.NewUserRequest;
import ru.practicum.explore.model.User;
import ru.practicum.explore.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserResponse {

    private final UserService userService;
    private final Mapper mapper;

    @Autowired
    public UserResponse(UserService userService, Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    public ResponseEntity<Object> getUsersAdmin(List<Long> ids, Integer from, Integer size) {
        List<User> list;
        try {
            list = userService.getUsersAdmin(ids, from, size);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("users"), HttpStatus.FORBIDDEN);
        }

        if (list.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
            //вот тут нужно оставить так,так как для тестов нужна проверка на пустой лист,а не ApiError если юзеров нет
        }
        List<UserDto> resultList = list.stream().map(mapper::fromUserToDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> addUserAdmin(NewUserRequest newUserRequest) {
        User user = mapper.fromUserRequestToUser(newUserRequest);
        User backUser;
        try {
            backUser = userService.addUserAdmin(user);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("user"), HttpStatus.FORBIDDEN);
        }

        if (backUser == null) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("user"), HttpStatus.NOT_FOUND);
        }
        UserDto userDto = mapper.fromUserToDto(backUser);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteUserAdmin(long userId) {
        User backUser;
        try {
            backUser = userService.deleteUserAdmin(userId);
        } catch (Exception e) {
            return new ResponseEntity<>(ForbiddenError.getForbidden("user"), HttpStatus.FORBIDDEN);
        }

        if (backUser == null) {
            return new ResponseEntity<>(NotFoundApiError.getNotFound("user"), HttpStatus.NOT_FOUND);
        }

        UserDto userDto = mapper.fromUserToDto(backUser);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}

