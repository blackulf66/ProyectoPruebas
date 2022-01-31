package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.payload.PostRequest;
import com.sopromadze.blogapi.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Lob;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    Category category;
    PostRequest postRequest;

    @BeforeEach
    void init() {
        category = new Category();
        category.setId(1L);
        category.setName("Nueva Categoría");

        postRequest = new PostRequest();
        postRequest.setCategoryId(1L);
        postRequest.setTitle("Nuevo Titulo");
        postRequest.setBody("El perro de San Roque no tiene rabo por que Ramón Ramírez se lo ha cortado");
    }
    
    static ResponseBodyMatchers responseBodyMatches() {
        return new ResponseBodyMatchers();
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void test_addPost_return400() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .contentType("application/json"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void test_addPost_return201() throws Exception {
        mockMvc.perform(post("/api/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isCreated());
        }

        @Test
        @WithMockUser(authorities = {"ROLE_ADMIN"})
    void test_addPost_return403() throws Exception {

            mockMvc.perform(post("/api/posts")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(postRequest)))
                    .andExpect(status().isForbidden());
        }


}