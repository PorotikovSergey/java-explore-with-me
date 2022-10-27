package ru.practicum.explore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.User;
import ru.practicum.explore.storage.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsersAdmin(List<Long> ids, Integer from, Integer size) {
        List<User> list = userRepository.findAllById(ids);
        List<User> afterPageableList = getPageableList(list, from, size);
        return afterPageableList;
    }

    public User addUserAdmin(User user) {
        userRepository.save(user);
        return user;
    }

    public User getUser(long id) {
        User user = userRepository.findById(id).get();
        return user;
    }

    public User deleteUserAdmin(long userId) {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            User user = optional.get();
            userRepository.deleteById(userId);
            return user;
        }
        return null;
    }

    private List<User> getPageableList(List<User> list, int from, int size) {
        PagedListHolder<User> page = new PagedListHolder<>(list.subList(from, list.size()));
        page.setPageSize(size);
        page.setPage(0);
        return page.getPageList();
    }
}
