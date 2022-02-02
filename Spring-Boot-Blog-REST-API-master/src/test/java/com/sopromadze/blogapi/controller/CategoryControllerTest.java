package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.service.CategoryService;
import com.sopromadze.blogapi.utils.AppUtils;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


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
    //TEST: devuelve todas las categorias que haya
    //ENTRADA: get /api/categories
    //RESULTADO ESPERADO: que devuelva un 200
    void test_getAll_Categories_Success() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("KratosCategory");


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
    //TEST: añade una categoria
    //ENTRADA: post /api/categories
    //RESULTADO ESPERADO: que devuelva un 200
    void test_addCategories_Success() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("KratosCategory");

        mockMvc.perform(post("/api/categories")
                .content(objectMapper.writeValueAsString(category))
                .contentType("application/json"))
         .andExpect(status().isOk()).andDo(print());


    }

    @Test
    @DisplayName("POST api/categories/")
    @WithMockUser(authorities = {"ROLE_OTRO"})
    //TEST: añade una categoria
    //ENTRADA: post /api/categories
    //RESULTADO ESPERADO: que devuelva un 403
    void test_addCategories_Fail() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("KratosCategory");

        mockMvc.perform(post("/api/categories")
                        .content(objectMapper.writeValueAsString(category))
                        .contentType("application/json"))
                .andExpect(status().isForbidden()).andDo(print());


    }

    @Test
    @DisplayName("GET api/categories/{id}")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    //TEST: obtiene las categorias por id
    //ENTRADA: get /api/categories{id}
    //RESULTADO ESPERADO: que devuelva un 200
    void test_get_Categories_ID_Success() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("KratosCategory");

        mockMvc.perform(get("/api/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(category))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }

    @Test
    @DisplayName("PUT api/categories/{id}")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    //TEST: obtiene las categorias por id
    //ENTRADA: get /api/categories{id}
    //RESULTADO ESPERADO: que devuelva un 200
    void test_put_Categories_Success() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("KratosCategory");

        mockMvc.perform(put("/api/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(category))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());

    }
    @Test
    @DisplayName("Delete api/categories/{id}")
    @WithMockUser(authorities = {"ROLE_ADMIN" , "ROLE_USER"})
        //TEST: que borre una categoria
        //ENTRADA: delete /api/categories{id}
        //RESULTADO ESPERADO: que devuelva un 204
    void test_Delete_Categories_Success() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("KratosCategory");

        mockMvc.perform(delete("/api/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(category))
                        .contentType("application/json"))
                .andExpect(status().isNoContent()).andDo(print());

    }

    @Test
    @DisplayName("Delete api/categories/{id}")
    @WithMockUser(authorities = {"ROLE_OTRO"})
        //TEST: que borre una categoria
        //ENTRADA: delete /api/categories{id}
        //RESULTADO ESPERADO: que devuelva un 403
    void test_Delete_Categories_fail() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("KratosCategory");

        mockMvc.perform(delete("/api/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(category))
                        .contentType("application/json"))
                .andExpect(status().isForbidden()).andDo(print());

    }

}