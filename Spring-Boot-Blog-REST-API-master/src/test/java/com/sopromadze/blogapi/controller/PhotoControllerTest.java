package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.PhotoRequest;
import com.sopromadze.blogapi.payload.PhotoResponse;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.method.P;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//TODO Hecho por Alfonso Gallardo
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
class PhotoControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;

    Photo photo;
    Album album;
    PagedResponse pagedResponse;
    PhotoRequest photoRequest;
    PhotoResponse photoResponse;

    @BeforeEach
    void init() {

        album = new Album();
        album.setId(1L);
        album.setTitle("Nuevo Album");

        photo = new Photo();
        photo.setId(1L);
        photo.setTitle("Nueva Foto");
        photo.setCreatedAt(Instant.now());
        photo.setUpdatedAt(Instant.now());

        photoResponse = new PhotoResponse(1L, "Nueva Foto", "www.nuevaPhoto.com", "esto nose lo que es", album.getId());

        photoRequest = new PhotoRequest();
        photoRequest.setAlbumId(album.getId());
        photoRequest.setTitle("Nueva Respuesta de Foto");
        photoRequest.setUrl("www.photoRequest.com");
        photoRequest.setThumbnailUrl("tblr");

        Role rol = new Role();
        rol.setName(RoleName.ROLE_ADMIN);

        List<Role> roles = Arrays.asList(rol);

        User user1 = new User();
        user1.setId(1L);
        user1.setRoles(roles);
        UserPrincipal user_prueba = new UserPrincipal(user1.getId(),"name","lastName","username","user_prueba@gmail.com","123456789",
                user1.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList()));


        pagedResponse = new PagedResponse(List.of(photo),1,1,1,1, true);

    }
    static ResponseBodyMatchers responseBodyMatches() {
        return new ResponseBodyMatchers();
    }

    /*
    Test: Obtener todas las fotos
    Entrada: /api/photos
    Salida: Devuelve un código
     */

    @Test
    void test_getAllPhotos () throws Exception {
        when(photoService.getAllPhotos(1,1)).thenReturn(pagedResponse);
        mockMvc.perform(get("/api/photos")
                .param("page", "1")
                .param("size", "1")
                .contentType("application/json"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponse)))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    void test_getPhoto () throws Exception {
        mockMvc.perform(get("/api/photos/{id}", 1L)
                        .content(objectMapper.writeValueAsString(photo))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void test_addPhoto () throws Exception {
        mockMvc.perform(post("/api/photos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(photoResponse)))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void test_addPhoto_return400 () throws Exception {
        mockMvc.perform(post("/api/photos")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void test_addPhoto_return403() throws Exception {
        mockMvc.perform(post("/api/photos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(photoResponse)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "ROLE_ADMIN"})
    void test_updatePhoto ()throws Exception {
        mockMvc.perform(put("/api/photos/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(photoResponse)))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "ROLE_ADMIN"})
    void test_updatePhoto_return400() throws Exception {
        mockMvc.perform(put("/api/photos/{id}",1L)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_OTRO"})
    void test_updatePhoto_return403() throws Exception {
        mockMvc.perform(put("/api/photos/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(photoResponse)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void test_deletePhoto () throws Exception {
        mockMvc.perform(delete("/api/photos/{id}", 1L)
                        .content(objectMapper.writeValueAsString(photo))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_OTRO"})
    void test_deletePhoto_return403() throws Exception {
        mockMvc.perform(delete("/api/photos/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(photo)))
                .andExpect(status().isForbidden());
    }

    @Test
    void test_deletePhoto_return401() throws Exception {
        mockMvc.perform(delete("/api/photo/{id}",1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(photo)))
                .andExpect(status().isUnauthorized());
    }


}