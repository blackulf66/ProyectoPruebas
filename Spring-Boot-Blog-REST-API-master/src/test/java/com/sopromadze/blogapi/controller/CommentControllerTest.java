package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.service.CategoryService;
import com.sopromadze.blogapi.service.CommentService;
import com.sopromadze.blogapi.utils.AppUtils;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
public class CommentControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private AppUtils appUtils;

    PagedResponse<Comment> pagedResponseComment;
    Comment comment;
    Post post;

    @BeforeEach
    void InitData(){

        post = new Post();
        post.setId(1L);

        comment = new Comment();
        comment.setId(1L);
        comment.setName("comment");
        comment.setEmail("emailcomment@gmail.com");
        comment.setBody("qwertyuiopasdfghjkl");

        post.setComments(List.of(comment));


    }


    @Test
    @WithUserDetails("admin")
    @DisplayName("GET /api/posts/{postId}/comments")
    void test_getAllCommentsSuccess() throws Exception {

        pagedResponseComment = new PagedResponse(List.of(comment), 1, 1, 1, 1, true);


        when(commentService.getAllComments(1L,1,1)).thenReturn(pagedResponseComment);
        mockMvc.perform(get("/api/posts/{id}/comments",1L)
                        .param("page", "1")
                        .param("size", "1")
                        .contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponseComment)))
                .andExpect(status().isOk()).andDo(print());

    }

    @Test
    @DisplayName("GET /api/posts/{postId}/comments/{id}")
    void test_getCommentSuccess() throws Exception {


        mockMvc.perform(get("/api/posts/{id}/comments/{id}", 1L,1L)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }


    @Test
    @DisplayName("POST /api/posts/{id}/comments")
    @WithMockUser(authorities = {"ROLE_USER"})
    void test_addCommentSuccess() throws Exception {

        Comment newComment = new Comment();
        newComment.setName("comment");


        mockMvc.perform(post("/api/posts/{id}/comments",1L)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType("application/json"))
                .andExpect(status().isCreated()).andDo(print());


    }

    @Test
    @DisplayName("PUT /api/posts/{id}/comments/{id}")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void test_putCommentSuccess() throws Exception {


        mockMvc.perform(put("/api/posts/{id}/comments/{id}", 1L,1L)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }

    @Test
    @DisplayName("Delete /api/posts/{id}/comments/{id}")
    @WithMockUser(authorities = {"ROLE_ADMIN" , "ROLE_USER"})
    void test_deleteCommentSuccess() throws Exception {


        mockMvc.perform(delete("/api/posts/{id}/comments/{id}", 1L,1L)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType("application/json"))
                .andExpect(status().isNoContent()).andDo(print());

    }


    @Test
    @DisplayName("POST /api/posts/{id}/comments")
    void test_addCommentFail() throws Exception {

        mockMvc.perform(post("/api/posts/{id}/comments",1L)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType("application/json"))
                .andExpect(status().isForbidden()).andDo(print());


    }

    @Test
    @DisplayName("Delete /api/posts/{id}/comments/{id}")
    void test_deleteCommentFail() throws Exception {


        mockMvc.perform(delete("/api/posts/{id}/comments/{id}", 1L,1L)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType("application/json"))
                .andExpect(status().isNoContent()).andDo(print());

    }




}
