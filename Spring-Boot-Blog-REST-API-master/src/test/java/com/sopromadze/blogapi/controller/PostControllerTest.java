package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.payload.PagedResponse;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private AppUtils appUtils;

    PagedResponse<Post> pagedResponsePost;
    Post post;

    @Test
    @WithUserDetails("admin")
    @DisplayName("GET api/posts/")
    void test_getAll_Categories_Success() throws Exception {

        post = new Post();
        post.setId(1L);
        post.setBody("body");

        pagedResponsePost = new PagedResponse(List.of(post), 1, 1, 1, 1, true);
        when(postService.getAllPosts(1,1)).thenReturn(pagedResponsePost);
        mockMvc.perform(get("/api/posts")
                        .param("page", "1")
                        .param("size", "1")
                        .contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponsePost)))
                .andExpect(status().isOk()).andDo(print());

    }

    /*@GetMapping("/category/{id}")
	public PagedResponse<Post> getPostsByCategory(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "id") Long id) {
		PagedResponse<Post> response = postService.getPostsByCategory(id, page, size);

		return response;
	}*/

    @Test
    @DisplayName("GET api/category/{id}")
    void test_get_CategoriesID_Success() throws Exception {

        post = new Post();
        post.setId(1L);
        post.setBody("chimichanga");


        pagedResponsePost = new PagedResponse(List.of(post), 1, 1, 1, 1, true);
        when(postService.getAllPosts(1,1)).thenReturn(pagedResponsePost);
        mockMvc.perform(get("/api/category/{id}")
                        .param("page", "1")
                        .param("size", "1")
                        .contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponsePost)))
                .andExpect(status().isOk()).andDo(print());

    }

}