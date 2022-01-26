package com.sopromadze.blogapi.service;

import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryServiceImplTest {

    @Mock
    private final ModelMapper modelMapper;
    CategoryServiceImplTest() {
        modelMapper = null;
    }

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    //TODO: Daniel de Luna Rodríguez
    @Test
    void test_getAllCategories(){

        Category cat1 = new Category("dummy_category");
        Category cat2 = new Category("dummy_category2");

        Page<Category> pageResult = new PageImpl<>(Arrays.asList(cat1));

        PagedResponse<Category> result = new PagedResponse<>();

        result.setContent(pageResult.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);

        List<Category> categories = new ArrayList<>();

        Category [] categoryResponses = {cat2};
        categories.add(cat1);

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(pageResult);
        when(modelMapper.map(pageResult.getContent(), Category[].class)).thenReturn(categoryResponses);

        assertEquals(result, categoryService.getAllCategories(1,1));
    }

    @Test
    void test_getCategorySuccess(){

        Category cat1 = new Category("dummy_category");

        cat1.setId(1L);

        categoryRepository.save(cat1);

        when(categoryRepository.getById(1L)).thenReturn(cat1);

        assertEquals(cat1, categoryRepository.getById(1L));


    }
}
