package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}