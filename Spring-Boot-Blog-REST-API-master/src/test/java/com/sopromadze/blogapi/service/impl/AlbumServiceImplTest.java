package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.BlogapiException;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    void test_deletedAlbumResourceNotFoundExeption_Success(){

        AlbumServiceImpl albumService1 = mock(AlbumServiceImpl.class);
        UserPrincipal user_prueba = mock(UserPrincipal.class);

        Album album = new Album();
        album.setTitle("album");
        album.setId(1L);
        albumRepository.save(album);

        doNothing().when(albumService1).deleteAlbum(isA(Long.class),isA(UserPrincipal.class));
        albumService1.deleteAlbum(1L,user_prueba);

        assertThrows(ResourceNotFoundException.class,()->albumService.deleteAlbum(2L ,user_prueba));

    }

    @Test
    void test_deletedAlbumExeption_Success(){

        Role rol = new Role();
        rol.setName(RoleName.ROLE_USER);

        List<Role> roles = Arrays.asList(rol);

        User user1 = new User();
        user1.setId(1L);
        user1.setRoles(roles);

        UserPrincipal user_prueba = new UserPrincipal(1L,"name","lastName","username","user_prueba@gmail.com","123456789",
                user1.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList()));

        AlbumServiceImpl albumService1 = mock(AlbumServiceImpl.class);

        Album album = new Album();
        album.setTitle("album");
        album.setId(1L);
        albumRepository.save(album);

        doNothing().when(albumService1).deleteAlbum(isA(Long.class),isA(UserPrincipal.class));
        albumService1.deleteAlbum(1L,user_prueba);

        assertThrows(ResourceNotFoundException.class,()->albumService.deleteAlbum(1323L ,user_prueba));

    }

    @Test
    void test_deletedAlbumExeption2_Success(){

        Role rol = new Role();
        rol.setName(RoleName.ROLE_USER);

        List<Role> roles = Arrays.asList(rol);

        User user1 = new User();
        user1.setId(1L);
        user1.setRoles(roles);

        User user2 = new User();
        user2.setId(2L);
        user2.setRoles(roles);

        UserPrincipal user_prueba = UserPrincipal.create(user1);

        UserPrincipal user_prueba2 = UserPrincipal.create(user2);

        AlbumServiceImpl albumService1 = mock(AlbumServiceImpl.class);

        Album album = new Album();
        album.setTitle("album");
        album.setId(1L);
        album.setUser(user1);
        albumRepository.save(album);

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(userRepository.getUser(any())).thenReturn(user1);

        doNothing().when(albumService1).deleteAlbum(isA(Long.class),isA(UserPrincipal.class));

        assertThrows(BlogapiException.class,()->albumService.deleteAlbum(1L ,user_prueba2));

    }

    @Test
    void test_getalbumThrowResourceNotFoundException(){
        when(albumRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->albumService.getAlbum(any(Long.class)));
    }

    @Test
    void test_updateAlbumSuccess() {

        UserPrincipal user_prueba;

        Role rol = new Role();
        rol.setName(RoleName.ROLE_ADMIN);

        List<Role> roles = Arrays.asList(rol);

        User user1 = new User();
        user1.setId(1L);
        user1.setRoles(roles);

        user_prueba = UserPrincipal.create(user1);
        Album album = new Album();
        album.setUser(user1);
        album.setId(1L);
        album.setCreatedBy(1l);
        when(albumRepository.save(album)).thenReturn(album);
        when(userRepository.getUser(user_prueba)).thenReturn(user1);

        AlbumRequest album2 = new AlbumRequest();
        album2.setId(2L);
        album2.setUser(user1);
        album2.setCreatedBy(1L);

        AlbumResponse albumResponse = new AlbumResponse();
        albumResponse.setId(2L);
        albumResponse.setTitle(null);
        albumResponse.setUser(user1);
        albumResponse.setPhoto(null);

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));
        when(userRepository.getUser((user_prueba))).thenReturn(user1);

        albumResponse.setTitle(album.getTitle());

        when(albumRepository.save(album)).thenReturn(album);


        when(modelMapper.map(any(),any())).thenReturn(albumResponse);

        assertEquals(albumResponse,albumService.updateAlbum(1L,album2,user_prueba));

    }



}