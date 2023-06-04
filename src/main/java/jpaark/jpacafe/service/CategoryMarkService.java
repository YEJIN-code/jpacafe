package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.CategoryMark;
import jpaark.jpacafe.domain.Post;
import jpaark.jpacafe.repository.CategoryMarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 조회 성능 최적화
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class CategoryMarkService {

    private final CategoryMarkRepository categoryMarkRepository;

    // 카페 생성
    @Transactional(readOnly = false)
    public Long join(CategoryMark categoryMark) {
        validateDuplicateCafe(categoryMark);
        categoryMarkRepository.save(categoryMark);

        return categoryMark.getCategory().getId();
    }

    // 중복 카페 검증
    public void validateDuplicateCafe(CategoryMark categoryMark) {
        List<CategoryMark> cafeList = categoryMarkRepository.findById(categoryMark.getCategory().getId());
        if (!cafeList.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 즐겨찾기입니다.");
        }
    }

    @Transactional
    public void deleteCategoryMark(Long categoryMarkId) {
        CategoryMark categoryMark = categoryMarkRepository.findOne(categoryMarkId);
        if (categoryMark != null) {
            categoryMarkRepository.delete(categoryMark);
        }
    }

    @Transactional
    public CategoryMark updateCategoryMark(Long categoryMarkId, int newPostCount) {
        CategoryMark findCategoryMark = categoryMarkRepository.findOne(categoryMarkId);
        findCategoryMark.setNewPostCount(newPostCount);

        return findCategoryMark;
    }

    public CategoryMark findOne(Long categoryMarkId) {
        return categoryMarkRepository.findOne(categoryMarkId);
    }

    public List<CategoryMark> findByUserId(String userId) {
        return categoryMarkRepository.findByUserId(userId);
    }

    public Long countByUserId(String userId) {
        return categoryMarkRepository.countByUserId(userId);
    }

    public List<CategoryMark> findByUserIdAndCategoryId(String userId, Long categoryId) {
        return categoryMarkRepository.findByUserIdAndCategoryId(userId, categoryId);
    }

}
