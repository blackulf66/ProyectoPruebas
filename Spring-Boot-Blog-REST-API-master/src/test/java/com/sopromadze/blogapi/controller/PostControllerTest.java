package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.PostRequest;
import com.sopromadze.blogapi.payload.PostResponse;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.service.CategoryService;
import com.sopromadze.blogapi.service.PostService;
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
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private AppUtils appUtils;

    PagedResponse<Post> pagedResponsePost;
    Post post;
    PostRequest postRequest;
    PostResponse postResponse;
    Category category;

    @Test
    @WithUserDetails("admin")
    @DisplayName("GET api/posts/")
        //TEST: obtener todas las categorias
        //ENTRADA: get /api/posts
        //RESULTADO ESPERADO: que devuelva un 200
    void test_getAll_Categories_Success() throws Exception {

        post = new Post();
        post.setId(1L);
        post.setBody("body");

        pagedResponsePost = new PagedResponse(List.of(post), 1, 1, 1, 1, true);
        when(postService.getAllPosts(1,1)).thenReturn(pagedResponsePost);
        mockMvc.perform(get("/api/posts" )
                        .param("page", "1")
                        .param("size", "1")
                        .contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponsePost)))
                .andExpect(status().isOk()).andDo(print());

    }

    @Test
    @DisplayName("GET api/posts/")
        //TEST: obtener las categorias de un post por id
        //ENTRADA: get /api/posts/category/{id}
        //RESULTADO ESPERADO: que devuelva un 200
    void test_get_CategoriesID_Success() throws Exception {

        post = new Post();
        post.setId(1L);
        post.setBody("chimichanga");

        Category category = new Category();
        category.setId(1L);
        category.setName("XMEN");

        pagedResponsePost = new PagedResponse(List.of(post), 1, 1, 1, 1, true);
        when(postService.getAllPosts(1,1)).thenReturn(pagedResponsePost);
        mockMvc.perform(get("/api/posts/category/{id}",1L)
                        .param("page", "1")
                        .param("size", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());

    }

    @Test
    @DisplayName("GET api/posts/{id}")
        //TEST: obtener las categorias  por id
        //ENTRADA: get /api/categories/{id}
        //RESULTADO ESPERADO: que devuelva un 200
    void test_get_Categories_ID_Success() throws Exception {

        post = new Post();
        post.setId(1L);
        post.setTitle("sdsdsadasdasdasdas");

        mockMvc.perform(get("/api/categories/{id}", 1L)
                        .content(objectMapper.writeValueAsString(post))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }
    @Test
    @DisplayName("GET api/posts/")
        //TEST: obtener los tags de un post por id
        //ENTRADA: get /api/posts/tag/{id}
        //RESULTADO ESPERADO: que devuelva un 200
    void test_get_Tag_Id_Success() throws Exception {

        post = new Post();
        post.setId(1L);
        post.setBody("body");

        Tag tag = new Tag();
        tag.setName("tag");
        tag.setId(1L);

        pagedResponsePost = new PagedResponse(List.of(post), 1, 1, 1, 1, true);
        when(postService.getAllPosts(1,1)).thenReturn(pagedResponsePost);
        mockMvc.perform(get("/api/posts/tag/{id}" , 1L)
                        .param("page", "1")
                        .param("size", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());

    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    @DisplayName("POST api/posts/")
        //TEST: agregar un post
        //ENTRADA: post /api/posts
        //RESULTADO ESPERADO: que devuelva un 200
    void test_addCategories_success() throws Exception {
        Category category = new Category();
        category.setName("casads");
        category.setId(1L);

        postRequest = new PostRequest();
        postRequest.setTitle("ªªªªªaaAaAaaa!Aª");
        postRequest.setBody("opdsasiadsadgfafgndoifpghdghdfrytrtyrtyrtyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyrp");
        postRequest.setCategoryId(1L);


        mockMvc.perform(post("/api/posts")
                        .content(objectMapper.writeValueAsString(postRequest))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }

    @Test
    @DisplayName("PUT api/posts/{id}")
    @WithMockUser(authorities = {"ROLE_ADMIN" , "ROLE_USER"})
        //TEST: actualizar un post ya agregado
        //ENTRADA: put /api/posts/{id}
        //RESULTADO ESPERADO: que devuelva un 200
    void test_put_Categories_Success() throws Exception {

        category = new Category();
        category.setId(1L);

        postRequest = new PostRequest();
        postRequest.setTitle("ªªªªªaaAaAaaa!Aª");
        postRequest.setBody("opdsasiadsadgfafgndoifpghdghdfrytrtyrtyrtyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyrp");
        postRequest.setCategoryId(1L);


        mockMvc.perform(put("/api/posts/{id}", 1L)
                        .content(objectMapper.writeValueAsString(postRequest))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());

    }

    @Test
    @DisplayName("Delete api/posts/{id}")
    @WithMockUser(authorities = {"ROLE_ADMIN" , "ROLE_USER"})
        //TEST: eliminar un post ya agregado
        //ENTRADA: update /api/posts/{id}
        //RESULTADO ESPERADO: que devuelva un 200
    void test_Delete_Categories_fail() throws Exception {
        Category category = new Category();
        category.setId(1L);

        post = new Post();
        post.setTitle("sdsdasdasda");
        post.setBody("dsiihja`pdhifdsàfdsafhdsfgds'afvbupsagdufsfgdspfgdsu");


        mockMvc.perform(delete("/api/posts/{id}", 1L)
                        .content(objectMapper.writeValueAsString(post))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());

    }




}