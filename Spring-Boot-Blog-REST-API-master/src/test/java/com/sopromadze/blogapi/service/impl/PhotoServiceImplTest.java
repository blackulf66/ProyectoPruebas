package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PhotoServiceImplTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    PhotoServiceImpl photoService;

    @Test
    void test_findAllPhotoService() {

        Photo photo = new Photo();
        photo.setTitle("Foto");

        Page<Photo> pageResult = new PageImpl<>(Arrays.asList(photo));

        PagedResponse<Photo> result = new PagedResponse<>();
        result.setContent(pageResult.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);


        assertEquals(result, photoService.getAllPhotos(0,10));

    }

}