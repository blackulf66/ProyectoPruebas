package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.PostRequest;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Lob;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    Post post;
    PagedResponse pagedResponsePost;
    Tag tag;

    @BeforeEach
    void init() {
        category = new Category();
        category.setId(1L);
        category.setName("Nueva Categoría");

        postRequest = new PostRequest();
        postRequest.setCategoryId(1L);
        postRequest.setTitle("Nuevo Titulo");
        postRequest.setBody("El perro de San Roque no tiene rabo por que Ramón Ramírez se lo ha cortado");

        post = new Post();
        post.setId(1L);
        post.setTitle("Tituelo");
        postRequest.setBody("El perro de San Roque no tiene rabo por que Ramón Ramírez se lo ha cortado");

        tag = new Tag();
        tag.setName("Nuevo tag");
        tag.setId(1L);

        Role rol = new Role();
        rol.setName(RoleName.ROLE_ADMIN);

        List<Role> roles = Arrays.asList(rol);

        User user1 = new User();
        user1.setId(1L);
        user1.setRoles(roles);
        UserPrincipal user_prueba = new UserPrincipal(user1.getId(),"name","lastName","username","user_prueba@gmail.com","123456789",
                user1.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList()));


        pagedResponsePost = new PagedResponse(List.of(post),1,1,1,1, true);
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
    void test_addPost() throws Exception {
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

        @Test
        @WithMockUser(authorities = {"ROLE_USER"})
        void test_getAllPost () throws Exception {

            when(postService.getAllPosts(1,1)).thenReturn(pagedResponsePost);
            mockMvc.perform(get("/api/posts")
                    .param("page", "1")
                    .param("size", "1")
                    .contentType("application/json"))
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(content().json(objectMapper.writeValueAsString(pagedResponsePost)))
                    .andExpect(status().isOk()).andDo(print());
        }



        @Test
        @WithMockUser(authorities = {"ROLE_ADMIN"})
        void test_getPost () throws Exception {

            mockMvc.perform(get("/api/posts/{id}", 1L)
                    .content(objectMapper.writeValueAsString(post))
                    .contentType("application/json"))
                    .andExpect(status().isOk()).andDo(print());
        }

        @Test
        @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
        void test_updatePost () throws Exception {
            mockMvc.perform(put("/api/posts/{id}", 1L)
                    .content(objectMapper.writeValueAsString(postRequest))
                    .contentType("application/json"))
                    .andExpect(status().isOk()).andDo(print());
        }

    @Test
    @WithMockUser(authorities = {"ROLE_OTRO"})
    void test_updatePost_return403() throws Exception {
        mockMvc.perform(put("/api/posts/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void test_updatePost_return401() throws Exception {
        mockMvc.perform(put("/api/posts/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isUnauthorized());
    }

        @Test
        @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
        void test_deletePost () throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 1L)
                .content(objectMapper.writeValueAsString(post))
                .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());
        }

    @Test
    @WithMockUser(authorities = {"ROLE_OTRO"})
    void test_deletePost_return403() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void test_deletePost_return401() throws Exception {
        mockMvc.perform(delete("/api/posts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void test_getPostByTag () throws Exception {
        when(postService.getAllPosts(1,1)).thenReturn(pagedResponsePost);
        mockMvc.perform(get("/api/posts/tag/{id}", 1L)
                .param("page", "1")
                .param("size", "1")
                .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());
    }


}