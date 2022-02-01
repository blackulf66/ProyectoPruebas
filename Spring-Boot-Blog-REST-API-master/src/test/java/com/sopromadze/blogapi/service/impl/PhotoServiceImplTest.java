package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.PhotoRequest;
import com.sopromadze.blogapi.payload.PhotoResponse;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.PhotoRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PhotoServiceImplTest {

    @Mock
    private PhotoRepository photoRepository;

    Album album;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private AlbumResponse albumResponse;

    @Mock
    private UserPrincipal userPrincipal;

    @Mock
    private final ModelMapper modelMapper;
    PhotoServiceImplTest() {
        modelMapper = null;
    }

    @InjectMocks
    PhotoServiceImpl photoService;

    @Test
    void test_findAllPhotoService() {

        album = new Album();
        album.setTitle("Nuevo Album");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());
        albumResponse = new AlbumResponse();
        albumResponse.setTitle("El album");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        Page<Album> page = new PageImpl<>(Arrays.asList(album));

        Photo photo = new Photo();
        photo.setTitle("Foto");
        photo.setAlbum(album);

        Page<Photo> pageResult = new PageImpl<>(Arrays.asList(photo));

        PagedResponse<Photo> result = new PagedResponse<>();
        result.setContent(pageResult.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);

        when(photoRepository.findAll(any(Pageable.class))).thenReturn(pageResult);
        assertNotEquals(0, photoService.getAllPhotos(1, 10).getSize());

    }

    @Test
    void test_getPhotoService(){

        album = new Album();
        album.setTitle("Nuevo Album");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());
        albumResponse = new AlbumResponse();
        albumResponse.setTitle("el album");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        Page<Album> page = new PageImpl<>(Arrays.asList(album));

        Photo photo = new Photo();
        photo.setId(1L);
        photo.setTitle("Foto");
        photo.setAlbum(album);

        PhotoResponse photoResponse = new PhotoResponse(1L, "Foto", null, null, album.getId());



        when(photoRepository.findById(any(Long.class))).thenReturn(Optional.of(photo));
        assertEquals(photoResponse, photoService.getPhoto(1L));

    }

    @Test
    void test_getAllPhotosByAlbumService () {

        album = new Album();
        album.setId(2L);
        album.setTitle("Nuevo Album");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());
        albumResponse = new AlbumResponse();
        albumResponse.setTitle("el album");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        Photo photo = new Photo();
        photo.setId(1L);
        photo.setTitle("Foto");
        photo.setAlbum(album);

        Page<Photo> pageResult = new PageImpl<>(Arrays.asList(photo));

        PagedResponse<Photo> result = new PagedResponse<>();
        result.setContent(pageResult.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);

        PhotoResponse photoResponse = new PhotoResponse(1L, "Foto", null, null, album.getId());

        Page<PhotoResponse> pageResult1 = new PageImpl<PhotoResponse>(Arrays.asList(photoResponse));

        PagedResponse<PhotoResponse> result1 = new PagedResponse<>();
        result1.setContent(pageResult1.getContent());
        result1.setTotalPages(1);
        result1.setTotalElements(1);
        result1.setLast(true);
        result1.setSize(1);

        when(photoRepository.findByAlbumId(any(Long.class), any(Pageable.class))).thenReturn(pageResult);
        assertEquals(result1, photoService.getAllPhotosByAlbum(2L,1,10));
    }

   @Test
    void Test_addAlbumService(){

        Role role = new Role(RoleName.ROLE_ADMIN);

       List<Role> listaRoles = Arrays.asList(role);

       User user = new User("Alfonso", "Gallardo", "Alfonsogr", "alfonsogr@gmail.com", "1234");
       user.setId(userPrincipal.getId());
       user.setRoles(listaRoles);

        album = new Album();
        album.setId(2L);
        album.setTitle("Nuevo Album");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());
        albumResponse = new AlbumResponse();
        albumResponse.setTitle("el album");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());
        album.setUser(user);

        PhotoRequest photoRequest = new PhotoRequest();
        photoRequest.setAlbumId(album.getId());
        photoRequest.setTitle("Foto de la playa");

        Photo photo = new Photo();
        photo.setId(1L);
        photo.setTitle("Foto de la playa");
        photo.setAlbum(album);


       PhotoResponse photoResponse = new PhotoResponse(1L, "Foto de la playa", null, null, album.getId());

       when(albumRepository.findById(any(Long.class))).thenReturn(Optional.of(album));
       when(photoRepository.findById(any(Long.class))).thenReturn(Optional.of(photo));
       when(photoRepository.save(photo)).thenReturn(photo);
       assertAll(
               ()-> assertEquals(photoResponse.getTitle() , photo.getTitle()) ,
               ()-> assertEquals(photoResponse.getUrl() , photo.getUrl()) ,
               ()-> assertEquals(photoResponse.getThumbnailUrl() , photo.getThumbnailUrl()) ,
               ()-> assertEquals(photoResponse.getAlbumId() , photo.getAlbum().getId())
       );
    }

    @Test
    void test_updatePhoto(){

        Role role = new Role(RoleName.ROLE_ADMIN);

        List<Role> listaRoles = Arrays.asList(role);

        User user = new User("Alfonso", "Gallardo", "Alfonsogr", "alfonsogr@gmail.com", "1234");
        user.setId(userPrincipal.getId());
        user.setRoles(listaRoles);

        album = new Album();
        album.setId(2L);
        album.setTitle("Nuevo Album");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());
        albumResponse = new AlbumResponse();
        albumResponse.setTitle("el album");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());
        album.setUser(user);

        PhotoRequest photoRequest = new PhotoRequest();
        photoRequest.setAlbumId(album.getId());
        photoRequest.setTitle("Foto de la playa");

        Photo photo = new Photo();
        photo.setId(1L);
        photo.setTitle("Foto");
        photo.setAlbum(album);

        PhotoResponse photoResponse = new PhotoResponse(1L, "Foto de la playa", null, null, album.getId());

        when(albumRepository.findById(any(Long.class))).thenReturn(Optional.of(album));
        when(photoRepository.findById(any(Long.class))).thenReturn(Optional.of(photo));
        when(photoRepository.save(photo)).thenReturn(photo);
        assertEquals(photoResponse, photoService.updatePhoto(1L,photoRequest,userPrincipal));

    }


    @Test
    void test_deletedPhoto_Success(){

        PhotoServiceImpl photoService = mock(PhotoServiceImpl.class);

        Photo photo = new Photo();
        photo.setId(1L);
        photo.setTitle("Foto");
        photo.setAlbum(album);

        Page<Photo> pageResult = new PageImpl<>(Arrays.asList(photo));

        PagedResponse<Photo> result = new PagedResponse<>();
        result.setContent(pageResult.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);



        when(photoRepository.findById(any(Long.class))).thenReturn(Optional.of(photo));
        photoService.deletePhoto(1L,userPrincipal);
        verify(photoService, times(1)).deletePhoto(1L, userPrincipal);

    }

}