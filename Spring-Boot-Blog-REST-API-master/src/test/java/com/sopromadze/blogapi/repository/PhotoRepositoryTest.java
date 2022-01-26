package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.utils.AppConstants;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Mock
    private Page<Photo> photoPage;

   /* @Test
    void test_findAlbumById(){

        Album album = new Album();
        album.setTitle("Álbum de prueba");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());
        album.setId(1L);

        Photo foto1 = new Photo("foto1","shorturl.at/abdsP","shorturl.at/abdsP",album);
        Photo foto2 = new Photo("foto2","shorturl.at/abdsP","shorturl.at/abdsP",album);
        Photo foto3 = new Photo("foto3","shorturl.at/abdsP","shorturl.at/abdsP",album);
        Photo foto4 = new Photo("foto4","shorturl.at/abdsP","shorturl.at/abdsP",album);


        repository.save(foto1);
        repository.save(foto2);
        repository.save(foto3);
        repository.save(foto4);


        Page<Photo> photos= Mockito.mock(Page.class);
        Mockito.when(repository.findByAlbumId(1L, Mockito.any())).thenReturn(photos);


    }*/

}
