package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.AlbumService;
import com.sopromadze.blogapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AlbumServiceImplTest {


    private AlbumService albumService;

    private Album album;
    private AlbumResponse albumResponse;
    
    private final ModelMapper modelMapper;
    @Autowired
    private AlbumRepository albumRepository;


    private UserService userService;


    private UserRepository userRepository;

    AlbumServiceImplTest() {
        modelMapper = null;
    }

    @Test
    void test_getAllAlbumsService() {

        album = new Album();
        album.setTitle("dasdsad");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());
        albumResponse = new AlbumResponse();
        albumResponse.setTitle("fdgfdgfdg");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        List<AlbumResponse> data2 = Arrays.asList(albumResponse);
        Page<Album> data = new PageImpl<>(Arrays.asList(album));

        PagedResponse<AlbumResponse> result = new PagedResponse<>();

        result.setContent(data2);
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);

        List<Album> albums = new ArrayList<>();
        albums.add(album);
        AlbumResponse [] albumResponses = {albumResponse};

        when(albumRepository.findAll(any(Pageable.class))).thenReturn(data);
        when(modelMapper.map(data.getContent(), AlbumResponse[].class)).thenReturn(albumResponses);

        assertEquals(result, albumService.getAllAlbums(1,1));

    }

   /* @Test
    void test_addAllAlbumService1() {

        User user = new User();
        user.setUsername("user");
        user.setId(1L);

        Album album = new Album();
        album.setTitle("El album");
        album.setUser(user);

        Page<Album> pageResult = new PageImpl<>(Arrays.asList(album));
        PagedResponse<Album> result = new PagedResponse<>();

        result.setContent(pageResult.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);

        when(albumRepository.findByCreatedBy(any(Long.class), any(Pageable.class))).thenReturn(pageResult);

        assertEquals(result, albumService.addAlbum(albumRequest, user));

    }*/
}