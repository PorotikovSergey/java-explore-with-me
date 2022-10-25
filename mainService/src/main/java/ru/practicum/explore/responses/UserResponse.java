package ru.practicum.explore.responses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Mapper;
import ru.practicum.explore.dto.UserDto;
import ru.practicum.explore.model.ApiError;
import ru.practicum.explore.model.NewUserRequest;
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
        List<User> list = userService.getUsersAdmin(ids, from, size);
        if(list.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        List<UserDto> resultList = list.stream().map(mapper::fromUserToDto).collect(Collectors.toList());
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    public ResponseEntity<Object> addUserAdmin(NewUserRequest newUserRequest) {
        User user = mapper.fromUserRequestToUser(newUserRequest);
        User backUser = userService.addUserAdmin(user);
        if(backUser==null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        UserDto userDto = mapper.fromUserToDto(backUser);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteUserAdmin(long userId) {
        User backUser = userService.deleteUserAdmin(userId);
        if(backUser==null) {
            return new ResponseEntity<>(new ApiError(), HttpStatus.NOT_FOUND);
        }
        UserDto userDto = mapper.fromUserToDto(backUser);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}

