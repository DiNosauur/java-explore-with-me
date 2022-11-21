package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.ConflictException;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Override
    public Collection<Category> getAllCategories() {
        return repository.findAll();
    }

    private void validate(Category category) {
        Optional<Category> foundCategory = repository.findByName(category.getName());
        if (foundCategory.isPresent() && foundCategory.get().getId() != category.getId()) {
            throw new ConflictException("Категория с таким именем уже зарегестрирована");
        }
    }

    @Transactional
    @Override
    public Category saveCategory(Category category) {
        log.info("Добавление категории {}", category.toString());
        return repository.save(category);
    }

    @Transactional
    @Override
    public Optional<Category> updateCategory(Category category) {
        log.info("Редактирование категории {}", category.toString());
        validate(category);
        Optional<Category> changeableCategory = getCategory(category.getId());
        if (changeableCategory.isPresent()) {
            if (category.getName() != null) {
                changeableCategory.get().setName(category.getName());
            }
            repository.save(changeableCategory.get());
        }
        return changeableCategory;
    }

    @Transactional
    @Override
    public boolean deleteCategory(long id) {
        log.info("Удаление категории (id={})", id);
        Optional<Category> category = getCategory(id);
        if (category.isPresent()) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<Category> getCategory(long id) {
        return repository.findById(id);
    }
}