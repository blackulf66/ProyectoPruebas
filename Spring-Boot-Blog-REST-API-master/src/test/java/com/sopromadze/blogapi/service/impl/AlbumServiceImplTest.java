package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.AlbumService;
import com.sopromadze.blogapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AlbumServiceImplTest {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Test
    void test_getAllAlbumsService() {

        Album album = new Album();
        album.setTitle("El album");

        Page<Album> pageResult = new PageImpl<>(Arrays.asList(album));
        PagedResponse<Album> result = new PagedResponse<>();

        result.setContent(pageResult.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);

        when(albumRepository.findByCreatedBy(any(Long.class), any(Pageable.class))).thenReturn(pageResult);

        assertEquals(result, albumService.getAllAlbums(0 , 1));

    }

    @Test
    void test_addAllAlbumService() {

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

    }
}