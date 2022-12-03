package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Validated
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/categories")
    public Collection<Category> getAllCategoriess() {
        return service.getAllCategories();
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> findCategoryById(@PathVariable long id) {
        return service.getCategory(id).map(category -> new ResponseEntity<>(category, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        return new ResponseEntity<>(service.saveCategory(category), HttpStatus.OK);
    }

    @PatchMapping("/admin/categories")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category category) {
        return service.updateCategory(category).map(updatedCategory -> new ResponseEntity<>(updatedCategory, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<Category> deleteCategoryById(@PathVariable long id) {
        return service.deleteCategory(id) ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
