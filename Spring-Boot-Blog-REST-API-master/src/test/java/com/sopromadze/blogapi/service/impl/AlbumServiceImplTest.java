package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.AlbumService;
import com.sopromadze.blogapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AlbumServiceImplTest {

    @InjectMocks
    private AlbumServiceImpl albumService;

    @Mock
    private final ModelMapper modelMapper;
    AlbumServiceImplTest() {
        modelMapper = null;
    }

    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void test_getAllAlbumsService() {

        Album album = new Album();
        album.setTitle("dasdsad");

        AlbumResponse albumResponse = new AlbumResponse();
        albumResponse.setTitle("fdgfdgfdg");

        Page<Album> pageResult = new PageImpl<>(Arrays.asList(album));
        Page<Album> pageResult2 = new PageImpl(Arrays.asList(albumResponse));

        PagedResponse<Album> result = new PagedResponse<>();

        result.setContent(pageResult2.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);

        List<Album> albums = new ArrayList<>();

        AlbumResponse [] albumResponses = {albumResponse};
        albums.add(album);

        when(albumRepository.findAll(any(Pageable.class))).thenReturn(pageResult);
        when(modelMapper.map(pageResult.getContent(), AlbumResponse[].class)).thenReturn(albumResponses);

        assertEquals(result, albumService.getAllAlbums(1,1));

    }

   @Test
    void test_getUserAlbumService() {

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

       when(userRepository.getUserByName("user")).thenReturn(user);
       when(albumRepository.findByCreatedBy(any(Long.class), any(Pageable.class))).thenReturn(pageResult);
       assertEquals(result, albumService.getUserAlbums("user", 0, 10));

    }
    @Test
    void Test_getAlbumService_succes(){

             Album album = new Album();
             album.setTitle("El album");
             album.setId(1L);

             when(albumRepository.getById(1L)).thenReturn(album);

             assertEquals(album, albumRepository.getById(1L));
    }

    @Test
    void Test_getAlbumService_fail(){

        Album album = new Album();
        album.setTitle("El album");
        album.setId(2L);

        when(albumRepository.getById(2L)).thenReturn(album);

        assertEquals(album, albumRepository.getById(1L));
    }

    @Test
    void Test_addAlbumService(){

            AlbumRequest albumRequest = new AlbumRequest();
            albumRequest.setTitle("El album");
            albumRequest.setId(1L);

            Album album2 = new Album();
            album2.setId(2L);
            album2.setTitle("ejhdfsifjsd");

            modelMapper.map(albumRequest, album2);

            UserPrincipal user1 = Mockito.mock(UserPrincipal.class);
            when(albumService.addAlbum(albumRequest,user1)).thenReturn(album2);
            assertEquals(album2, albumService.addAlbum(albumRequest,user1));

    }
    @Test
    void test_deletedAlbum_Success(){

        AlbumServiceImpl albumService1 = mock(AlbumServiceImpl.class);
        UserPrincipal user_prueba = mock(UserPrincipal.class);

        Album album = new Album();
        album.setTitle("album");
        album.setId(1L);
        albumRepository.save(album);

        doNothing().when(albumService1).deleteAlbum(isA(Long.class),isA(UserPrincipal.class));
        albumService1.deleteAlbum(1L,user_prueba);

        verify(albumService1, times(1)).deleteAlbum(1L, user_prueba);

    }

    @Test
    void test_deletedAlbum_fail(){

        AlbumServiceImpl albumService1 = mock(AlbumServiceImpl.class);
        UserPrincipal user_prueba = mock(UserPrincipal.class);

        Album album = new Album();
        album.setTitle("album");
        album.setId(1L);
        albumRepository.save(album);

        doNothing().when(albumService1).deleteAlbum(isA(Long.class),isA(UserPrincipal.class));
        albumService1.deleteAlbum(1L,user_prueba);

        verify(albumService1, times(1)).deleteAlbum(2L, user_prueba);

    }
}