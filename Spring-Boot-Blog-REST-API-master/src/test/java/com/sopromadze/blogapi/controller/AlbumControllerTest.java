package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.AlbumService;
import com.sopromadze.blogapi.utils.AppUtils;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlbumService albumService;

    @MockBean
    private AppUtils appUtils;

    Album album;
    AlbumRequest albumRequest;
    AlbumResponse albumResponse;
    PagedResponse<AlbumResponse> pagedResponseAlbum;
    User user;
    List<Photo> listaFotos;

    @BeforeEach
    void initTest(){

        Role rol = new Role();
        rol.setName(RoleName.ROLE_ADMIN);

        List<Role> roles = Arrays.asList(rol);

        User user1 = new User();
        user1.setId(1L);
        user1.setRoles(roles);

        UserPrincipal user_prueba = new UserPrincipal(user1.getId(),"name","lastName","username","user_prueba@gmail.com","123456789",
                user1.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList()));


        listaFotos = new ArrayList<>();

        user = new User();
        user.setId(3L);
        user.setRoles(roles);

        album = new Album();
        album.setId(1L);
        album.setTitle("Álbum de prueba");

        albumRequest = new AlbumRequest();
        albumRequest.setId(2L);

        albumResponse = new AlbumResponse();
        albumResponse.setId(2L);
        albumResponse.setTitle("AlbumResponse");
        albumResponse.setUser(user);
        albumResponse.setPhoto(listaFotos);


        pagedResponseAlbum = new PagedResponse(List.of(album), 1, 1, 1, 1, true);
    }


    @Test
    @WithUserDetails("admin")
    @DisplayName("GET api/album/")
    void test_getAllAlbumsSuccess() throws Exception {
        when(albumService.getAllAlbums(1,1)).thenReturn(pagedResponseAlbum);
        mockMvc.perform(get("/api/albums")
                        .param("page", "1")
                        .param("size", "1")
                        .contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponseAlbum)))
                .andExpect(status().isOk()).andDo(print());

    }


    @Test
    @DisplayName("POST api/albums")
    @WithMockUser(authorities = {"ROLE_USER"})
    void test_addAlbumSuccess() throws Exception {

        mockMvc.perform(post("/api/albums")
                        .content(objectMapper.writeValueAsString(album))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }

    @Test
    @DisplayName("GET api/albums/{id}")
    void test_getAlbumSuccess() throws Exception {


        mockMvc.perform(get("/api/albums/{id}", 1L)
                        .content(objectMapper.writeValueAsString(album))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }

    @Test
    @DisplayName("PUT api/albums/{id}")
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void test_updateAlbumSuccess() throws Exception {

        mockMvc.perform(put("/api/albums/{id}", 1L)
                        .content(objectMapper.writeValueAsString(album))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }

    @Test
    @DisplayName("Delete api/albums/{id}")
    @WithMockUser(authorities = {"ROLE_ADMIN" , "ROLE_USER"})
    void test_deleteAlbumSuccess() throws Exception {

        mockMvc.perform(delete("/api/albums/{id}", 1L)
                        .content(objectMapper.writeValueAsString(album))
                        .contentType("application/json"))
                .andExpect(status().isNoContent()).andDo(print());

    }

    @Test
    @DisplayName("POST api/albums")
    void test_addAlbumFail() throws Exception {

        mockMvc.perform(post("/api/albums")
                        .content(objectMapper.writeValueAsString(album))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());


    }

    @Test
    @DisplayName("Delete api/albums/{id}")
    void test_deleteAlbumFail() throws Exception {

        mockMvc.perform(delete("/api/albums/{id}", 1L)
                        .content(objectMapper.writeValueAsString(album))
                        .contentType("application/json"))
                .andExpect(status().isNoContent()).andDo(print());

    }


}