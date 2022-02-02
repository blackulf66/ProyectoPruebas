package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.PostService;
import com.sopromadze.blogapi.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
class TagControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    Tag tag;
    PagedResponse pagedResponse;

    @BeforeEach
    void inti () {
        tag = new Tag();
        tag.setName("Nuevo tag");
        tag.setId(1L);

        pagedResponse = new PagedResponse(List.of(tag),1,1,1,1, true);

        Role rol = new Role();
        rol.setName(RoleName.ROLE_ADMIN);

        List<Role> roles = Arrays.asList(rol);

        User user1 = new User();
        user1.setId(1L);
        user1.setRoles(roles);
        UserPrincipal user_prueba = new UserPrincipal(user1.getId(),"name","lastName","username","user_prueba@gmail.com","123456789",
                user1.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList()));


    }

    @Test
    void test_getAllTags () throws Exception {
        when(tagService.getAllTags(1,1)).thenReturn(pagedResponse);
        mockMvc.perform(get("/api/tags")
                .param("page", "1")
                .param("size", "1")
                .contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponse)))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    void test_getTag () throws Exception {
        mockMvc.perform(get("/api/tags/{id}", 1L)
                .content(objectMapper.writeValueAsString(tag))
                .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void test_addTag () throws Exception {
        mockMvc.perform(post("/api/tags")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void test_addTag_return400 () throws Exception {
        mockMvc.perform(post("/api/tags")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void test_addTag_return403() throws Exception {
        mockMvc.perform(post("/api/tags")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void test_updateTag () throws Exception {
        mockMvc.perform(put("/api/tags/{id}", 1L)
                        .content(objectMapper.writeValueAsString(tag))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());
    }


    @Test
    @WithMockUser(authorities = {"ROLE_OTRO"})
    void test_updateTag_return403() throws Exception {
        mockMvc.perform(put("/api/tags/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isForbidden());
    }

    @Test
    void test_updateTag_return401() throws Exception {
        mockMvc.perform(put("/api/tags/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void test_deleteTag () throws Exception {
        mockMvc.perform(delete("/api/tags/{id}", 1L)
                        .content(objectMapper.writeValueAsString(tag))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    void test_deleteTag_return401() throws Exception {
        mockMvc.perform(delete("/api/tags")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_OTRO"})
    void test_deletePost_return403() throws Exception {
        mockMvc.perform(delete("/api/tags/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isForbidden());
    }

}