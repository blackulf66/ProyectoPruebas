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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    Category category;

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
    @DisplayName("GET api/posts/")
    void test_get_CategoriesID_Success() throws Exception {

        post = new Post();
        post.setId(1L);
        post.setBody("chimichanga");

        category = new Category();
        category.setId(1L);
        category.setName("XMEN");

        pagedResponsePost = new PagedResponse(List.of(post), 1, 1, 1, 1, true);
        when(postService.getAllPosts(1,1)).thenReturn(pagedResponsePost);
        mockMvc.perform(get("/api/posts/category/{id}")
                        .param("page", "1")
                        .param("size", "1")
                        .contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponsePost)))
                .andExpect(status().isOk()).andDo(print());

    }

    /*	@GetMapping("/tag/{id}")
	public PagedResponse<Post> getPostsByTag(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "id") Long id) {
		PagedResponse<Post> response = postService.getPostsByTag(id, page, size);

		return response;
	}*/

    @Test
    @DisplayName("GET api/posts/")
    void test_get_Tag_Id_Success() throws Exception {

        post = new Post();
        post.setId(1L);
        post.setBody("body");

        pagedResponsePost = new PagedResponse(List.of(post), 1, 1, 1, 1, true);
        when(postService.getAllPosts(1,1)).thenReturn(pagedResponsePost);
        mockMvc.perform(get("/api/posts/tags{id}")
                        .param("page", "1")
                        .param("size", "1")
                        .param("name")
                        .contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponsePost)))
                .andExpect(status().isOk()).andDo(print());

    }

    /*@PostMapping
	@PreAuthorize("hasRole('USER')")
	public Category addCategory(@Valid @RequestBody Category category,
			@CurrentUser UserPrincipal currentUser) {
		return categoryService.addCategory(category, currentUser);
	}*/
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    @DisplayName("POST api/posts/")
    void test_addCategories_success() throws Exception {

        category = new Category();
        category.setName("casads");
        category.setId(1L);

        post  = new Post();
        post.setId(1L);
        post.setBody("dingididingi says sausage dog duoqsoyiudashoiudhoiuahoiudshoauidhoisahoidshoiadhosaohdhosahodoihas");
        post.setCategory(category);
        post.setTitle("dingididingi says sausage dog");


        mockMvc.perform(post("/api/posts")
                        .content(objectMapper.writeValueAsString(post))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }




}