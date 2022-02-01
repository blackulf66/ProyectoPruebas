package com.sopromadze.blogapi.service.impl;


import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.TagRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
        UserPrincipal usuario_prueba = Mockito.mock(UserPrincipal.class);
        when(tagService.addTag(tag,usuario_prueba)).thenReturn(tag);
        assertEquals(tag,tagService.addTag(tag,usuario_prueba));
    }


    @Test
    void updateTag(){


    }

}
