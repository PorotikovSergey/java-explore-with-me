package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.model.User;
import ru.practicum.explore.storage.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private static final String USER_NOT_FOUND = "Юзера по данному id нет в базе";
    private final UserRepository userRepository;

    public List<User> getUsersAdmin(List<Long> ids, Integer from, Integer size) {
        List<User> list = userRepository.findAllById(ids);
        return getPageableList(list, from, size);
    }

    public User addUserAdmin(User user) {
        userRepository.save(user);
        return user;
    }

    public User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    public User deleteUserAdmin(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        userRepository.deleteById(userId);
        return user;
    }

    private List<User> getPageableList(List<User> list, int from, int size) {
        PagedListHolder<User> page = new PagedListHolder<>(list.subList(from, list.size()));
        page.setPageSize(size);
        page.setPage(0);
        return page.getPageList();
    }
}
