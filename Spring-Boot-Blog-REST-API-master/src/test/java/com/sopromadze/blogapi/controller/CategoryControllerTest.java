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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


    @Test
    @DisplayName("POST api/categories/")
    @WithMockUser(authorities = {"ROLE_USER"})
    void test_addCategories_Success() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("MIMIR CALLATE YA");

        mockMvc.perform(post("/api/categories")
                .content(objectMapper.writeValueAsString(category))
                .contentType("application/json"))
         .andExpect(status().isOk()).andDo(print());


    }

    @Test
    @DisplayName("POST api/categories/")
    void test_addCategories_Fail() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("MIMIR CALLATE YA");

        mockMvc.perform(post("/api/categories")
                        .content(objectMapper.writeValueAsString(category))
                        .contentType("application/json"))
                .andExpect(status().isForbidden()).andDo(print());


    }

    @Test
    @DisplayName("GET api/categories/{id}")
    void test_get_Categories_ID_Success() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("MIMIR CALLATE YA");

        mockMvc.perform(get("/api/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(category))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }

    @Test
    @DisplayName("PUT api/categories/{id}")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void test_put_Categories_Success() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("MIMIR CALLATE YA");

        mockMvc.perform(put("/api/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(category))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }

    @Test
    @DisplayName("Delete api/categories/{id}")
    @WithMockUser(authorities = {"ROLE_ADMIN" , "ROLE_USER"})
    void test_Delete_Categories_Success() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("MIMIR CALLATE YA");

        mockMvc.perform(delete("/api/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(category))
                        .contentType("application/json"))
                .andExpect(status().isNoContent()).andDo(print());

    }

    @Test
    @DisplayName("Delete api/categories/{id}")
    void test_Delete_Categories_fail() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("MIMIR CALLATE YA");

        mockMvc.perform(delete("/api/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(category))
                        .contentType("application/json"))
                .andExpect(status().isNoContent()).andDo(print());

    }

}