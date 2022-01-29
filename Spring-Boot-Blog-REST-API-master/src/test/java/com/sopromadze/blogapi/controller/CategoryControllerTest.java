package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.CategoryService;
import com.sopromadze.blogapi.utils.AppUtils;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
class CategoryControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private AppUtils appUtils;

    PagedResponse<Category> pagedResponseCategories;
    Category category;

    @Test
    @WithUserDetails("admin")
    @DisplayName("GET api/categories/")
    void test_getAll_Categories_Success() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("MIMIR CALLATE YA");


        pagedResponseCategories = new PagedResponse(List.of(category), 1, 1, 1, 1, true);


    when(categoryService.getAllCategories(1,1)).thenReturn(pagedResponseCategories);
        mockMvc.perform(get("/api/categories")
                        .param("page", "1")
                        .param("size", "1")
                        .contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponseCategories)))
                .andExpect(status().isOk()).andDo(print());

    }


}