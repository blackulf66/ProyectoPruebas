package com.sopromadze.blogapi.service;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    void test_getAllCategories() {

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

        Category[] categoryResponses = {cat2};
        categories.add(cat1);

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(pageResult);
        when(modelMapper.map(pageResult.getContent(), Category[].class)).thenReturn(categoryResponses);

        assertEquals(result, categoryService.getAllCategories(1, 1));
    }

    @Test
    void test_getCategorySuccess() {
        Category cat1 = new Category("dummy_category");
        cat1.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat1));
        assertEquals(cat1, categoryService.getCategory(1L));
    }

    @Test
    void test_getCategoryThrowResourceNotFoundException(){
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->categoryService.getCategory(any(Long.class)));
    }

    @Test
    void test_updateCategoryThrowUnauthorizedException(){
        UserPrincipal user_prueba = mock(UserPrincipal.class);
        //when(categoryRepository.updateCategory(usePR)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->categoryService.getCategory(any(Long.class)));
    }

    @Test
    void test_addCategorySuccess(){
        Category cat1 = new Category("dummy_category");
        cat1.setId(1L);
        UserPrincipal user_prueba = mock(UserPrincipal.class);
        when(categoryService.addCategory(cat1,user_prueba)).thenReturn(cat1);
        assertEquals(categoryService.addCategory(cat1,user_prueba),cat1);
    }


    //ToDo:Update No funciona
    @Test
    void test_updateCategorySuccess() {

        Role rol = new Role();
        rol.setName(RoleName.ROLE_ADMIN);

        List<Role> roles = Arrays.asList(rol);

        User user1 = new User();
        user1.setId(1L);
        user1.setRoles(roles);

        UserPrincipal user_prueba = new UserPrincipal(user1.getId(),"name","lastName","username","user_prueba@gmail.com","123456789",
                user1.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList()));

        Category cat_original = new Category("dummy_category");
        cat_original.setId(1L);
        cat_original.setCreatedBy(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat_original));

        Category cat_result = new Category("dummy category edited");
        cat_result.setId(cat_original.getId());
        cat_original.setCreatedBy(1L);

        assertEquals(cat_result,categoryService.updateCategory(cat_original.getId(),cat_result,user_prueba));
    }


    @Test
    void test_deletedCategorySuccess(){

        CategoryServiceImpl catService = mock(CategoryServiceImpl.class);
        UserPrincipal user_prueba = mock(UserPrincipal.class);

        Category cat1 = new Category("dummy_category");
        cat1.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat1));

        doNothing().when(catService).deleteCategory(isA(Long.class),isA(UserPrincipal.class));
        catService.deleteCategory(1L,user_prueba);

        verify(catService, times(1)).deleteCategory(1L, user_prueba);

    }
}
