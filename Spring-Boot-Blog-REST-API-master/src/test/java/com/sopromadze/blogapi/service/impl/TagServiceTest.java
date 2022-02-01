package com.sopromadze.blogapi.service.impl;


import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.TagRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TagServiceTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private final ModelMapper modelMapper;
    TagServiceTest() {
        modelMapper = null;
    }

    @Mock
    private TagRepository tagRepository;

    Tag tag;
    PagedResponse<Tag> result;
    Page<Tag> pageResult;

    UserPrincipal user_prueba;
    User user1;

    @BeforeEach
    void InitData(){
        tag = new Tag();
        tag.setName("tag de prueba");
        tag.setId(1L);

        pageResult = new PageImpl<>(Arrays.asList(tag));

        result = new PagedResponse<>();

        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);
        result.setContent(List.of(tag));

        List<Album> albums = new ArrayList<>();

        Role rol = new Role();
        rol.setName(RoleName.ROLE_ADMIN);

        List<Role> roles = Arrays.asList(rol);

        user1 = new User();
        user1.setId(1L);
        user1.setRoles(roles);

        user_prueba = new UserPrincipal(user1.getId(),"name","lastName","username","user_prueba@gmail.com","123456789",
                user1.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList()));
    }

    @Test
    void test_getAllTagsSuccess() {


        when(tagRepository.findAll(any(Pageable.class))).thenReturn(pageResult);

        assertEquals(result, tagService.getAllTags(1,1));

    }

    @Test
    void getTagSuccess(){
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        assertEquals(tag, tagService.getTag(1L));

    }

    @Test
    void test_getTagThrowResourceNotFoundException(){
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        assertThrows(ResourceNotFoundException.class,()-> tagService.getTag(2L));
    }

    @Test
    void test_addTagSuccess(){
        when(tagService.addTag(tag,user_prueba)).thenReturn(tag);
        assertEquals(tag,tagService.addTag(tag,user_prueba));
    }


    @Test
    void test_updateTagSuccess(){


        Tag tag1 = new Tag("tag de prueba");
        tag1.setId(1L);
        tag1.setCreatedBy(1L);
        when(tagRepository.save(tag1)).thenReturn(tag1);

        Tag tag2 = new Tag("tag de prueba editado");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag1));

        assertEquals(tag1,tagService.updateTag(1L,tag2,user_prueba));

    }

    @Test
    void test_updateTagThrowResourceNotFoundException(){

        Tag tag1 = new Tag("tag de prueba");
        tag1.setId(1L);
        tag1.setCreatedBy(1L);
        when(tagRepository.save(tag1)).thenReturn(tag1);

        Tag tag2 = new Tag("tag de prueba editado");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag1));

        assertThrows(ResourceNotFoundException.class,()->tagService.updateTag(2L,tag2,user_prueba));

    }

    @Test
    void test_updateTagThrowUnauthorizedException(){

        UserPrincipal user_prueba2 = new UserPrincipal(2L,"name","lastName","username","user_prueba@gmail.com","123456789",
                user1.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_USER")).collect(Collectors.toList()));

        Tag tag1 = new Tag("tag de prueba");
        tag1.setId(1L);
        tag1.setCreatedBy(1L);
        when(tagRepository.save(tag1)).thenReturn(tag1);

        Tag tag2 = new Tag("tag de prueba editado");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag1));

        assertThrows(UnauthorizedException.class,()->tagService.updateTag(1L,tag2,user_prueba2));

    }

    @Test
    void test_deleteTagSuccess(){

        Tag tag1 = new Tag("tag de prueba");
        tag1.setId(1L);
        tag1.setCreatedBy(1L);
        when(tagRepository.save(tag1)).thenReturn(tag1);
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag1));
        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE, "You successfully deleted tag");

        assertEquals(apiResponse,tagService.deleteTag(1L,user_prueba));

    }

}
