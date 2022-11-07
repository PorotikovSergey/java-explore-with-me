package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.model.User;
import ru.practicum.explore.storage.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private static final String USER_NOT_FOUND = "Юзера по данному id нет в базе";
    private final UserRepository userRepository;

    public List<User> getUsersAdmin(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<User> result = userRepository.findAllByIdIn(ids, pageable).getContent();
        log.info("Список юзеров из бд найденных по айдишникам: {}", result);
        return result;
    }

    public User addUserAdmin(User user) {
        userRepository.save(user);
        log.info("В бд сохранён юзер: {}", user);
        return user;
    }

    public User getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        log.info("Из бд получен юзер: {}", user);
        return user;
    }

    public void deleteUserAdmin(long userId) {
        userRepository.deleteById(userId);
        log.info("Юзер удалён по id {}", userId);
    }
}
