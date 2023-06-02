package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Category;
import jpaark.jpacafe.domain.Post;
import jpaark.jpacafe.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = false)
    public Long join(Category category) {
        validateDuplicateCategoryName(category);
        categoryRepository.save(category);
        return category.getId();
    }

    private void validateDuplicateCategoryName(Category category) {
        List<Category> findCategories = categoryRepository.findByName(category.getName());
        if (!findCategories.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 카테고리입니다.");
        }
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        if (category != null) {
            categoryRepository.delete(category);
        }
    }

    public Category findOne(Long categoryId) {
        return categoryRepository.findOne(categoryId);
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findOne(categoryId);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findAllByCafeId(Long cafeId) {
        return categoryRepository.findByCafeId(cafeId);
    }

    public List<Category> findByName(String name) {
        return categoryRepository.findByName((name));
    }
}
