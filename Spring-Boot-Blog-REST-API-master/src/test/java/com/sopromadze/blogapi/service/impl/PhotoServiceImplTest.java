package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.time.Instant;
import java.util.Arrays;
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
    private AlbumResponse albumResponse;

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

        Page<Album> page = new PageImpl<>(Arrays.asList(album));

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

        when(photoRepository.findByAlbumId(any(Long.class), any(Pageable.class))).thenReturn(pageResult);
        assertEquals(result, photoService.getAllPhotosByAlbum(2L,1,10));
    }

   /* @Test
    void Test_addAlbumService(){

        PhotoRequest photoRequest = new PhotoRequest();
        photoRequest.setAlbumId(album.getId());
        photoRequest.setTitle("Foto de la playa");

        Photo photo = new Photo();
        photo.setId(1L);
        photo.setTitle("Foto");
        photo.setAlbum(album);

        modelMapper.map(photoRequest, photo);

        UserPrincipal userPrincipal = Mockito.mock(UserPrincipal.class);
        when(photoService.addPhoto(photoRequest,userPrincipal)).thenReturn(photo);
        assertEquals(photo, photoService.addPhoto(photoRequest,userPrincipal));

    }

    */
    @Test
    void test_deletedAlbum_Success(){

        PhotoServiceImpl albumService1 = mock(PhotoServiceImpl.class);
        UserPrincipal user_prueba = mock(UserPrincipal.class);

        Photo photo = new Photo();
        photo.setId(1L);
        photo.setTitle("Foto");
        photo.setAlbum(album);
        photoRepository.save(photo);

        doNothing().when(photoService).deletePhoto(isA(Long.class),isA(UserPrincipal.class));
        albumService1.deletePhoto(1L,user_prueba);

        verify(albumService1, times(1)).deletePhoto(1L, user_prueba);

    }

}