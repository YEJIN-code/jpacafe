package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Cafe;
import jpaark.jpacafe.domain.Category;
import jpaark.jpacafe.domain.Grade;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Status.StatusSet;
import jpaark.jpacafe.repository.CafeRepository;
import jpaark.jpacafe.repository.CategoryRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CafeRepository cafeRepository;


    @Test(expected = IllegalStateException.class)
    public void 카테고리_중복_검사() throws Exception {
        // given
        Category category1 = new Category();
        category1.setName("aaa");

        Category category2 = new Category();
        category2.setName("aaa");

        categoryService.join(category1);
        categoryService.join(category2);

        // then
        Assert.fail("예외가 발생해야 한다.");
    }

    @Test
    public void 카페_전체카테고리_찾기()  throws Exception {
        // Given
        Cafe cafe = new Cafe();
        cafe.setName("정통");
        cafeRepository.save(cafe); // Cafe 저장

        Category category1 = new Category();
        category1.setName("aaa");
        category1.setCafe(cafe);

        Category category2 = new Category();
        category2.setName("bbb");
        category2.setCafe(cafe);

        categoryService.join(category1);
        categoryService.join(category2);

        // When
        List<Category> categories = categoryService.findAllByCafeId(cafe.getId());

        // Then
        assertEquals(2, categories.size());
    }

    @Test
    public void 게시판_삭제권한_검사() throws Exception {
        // given
        Grade grade = new Grade();
        grade.setCategoryPermission(StatusSet.OFF);

        Member member = new Member();
        member.setGrade(grade);

        Category category1 = new Category();
        category1.setName("category");

        // when


    }

}
