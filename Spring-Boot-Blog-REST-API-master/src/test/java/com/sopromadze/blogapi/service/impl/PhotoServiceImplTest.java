package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.parameters.P;

import static org.junit.jupiter.api.Assertions.*;
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

    }

}