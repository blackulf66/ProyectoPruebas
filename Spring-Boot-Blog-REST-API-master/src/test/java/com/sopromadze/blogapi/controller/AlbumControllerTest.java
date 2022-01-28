package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.Album;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    Page result1element,result1elementRequest;
    PagedResponse<AlbumResponse> pagedResponseAlbum;
    User user;
    List<Photo> listaFotos;
    UserPrincipal userPrincipal;

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



}
