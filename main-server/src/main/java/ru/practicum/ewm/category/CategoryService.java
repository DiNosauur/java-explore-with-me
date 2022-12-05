package ru.practicum.ewm.category;

import java.util.Collection;
import java.util.Optional;

public interface CategoryService {
    Collection<Category> getAllCategories();

    Category saveCategory(Category category);

    Optional<Category> updateCategory(Category category);

    boolean deleteCategory(long id);

    Optional<Category> getCategory(long id);
}
