package ru.practicum.explore.storage;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
