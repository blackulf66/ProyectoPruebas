package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;

import com.sopromadze.blogapi.utils.AppConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void test_NotNull(){ assertNotNull(photoRepository);}

    @Test
    void test_PhotoRepository_success() {

        Album album = new Album();
        album.setTitle("album1");
        album.setUpdatedAt(Instant.now());
        album.setCreatedAt(Instant.now());


        Photo photo = new Photo();
        photo.setTitle("titulon");
        photo.setAlbum(album);
        photo.setThumbnailUrl("queurltanbonita");
        photo.setUrl("QuieroMasFotosDeSpider-manDijoJJJ");
        photo.setUpdatedAt(Instant.now());
        photo.setCreatedAt(Instant.now());

        testEntityManager.persist(album);

        testEntityManager.persist(photo);

        Page<Photo> comments = new PageImpl<>(Arrays.asList(photo));
        PageRequest pageRequest = PageRequest.of(1, 2);

        assertEquals(0, albumRepository.findByCreatedBy(album.getId(), pageRequest).getTotalElements());

    }

    @Test
    void test_findAlbumById(){

        Album album = new Album();
        album.setTitle("Álbum de prueba");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        testEntityManager.persist(album);


        Photo foto1 = new Photo();
        foto1.setTitle("foto1");
        foto1.setThumbnailUrl("shorturl.at/abdsP");
        foto1.setUrl("shorturl.at/abdsP");
        foto1.setAlbum(album);
        foto1.setCreatedAt(Instant.now());
        foto1.setUpdatedAt(Instant.now());
        Photo foto2 = new Photo("foto2","shorturl.at/abdsP","shorturl.at/abdsP",album);
        foto2.setTitle("foto2");
        foto2.setThumbnailUrl("shorturl.at/abdsP");
        foto2.setUrl("shorturl.at/abdsP");
        foto2.setAlbum(album);
        foto2.setCreatedAt(Instant.now());
        foto2.setUpdatedAt(Instant.now());
        Photo foto3 = new Photo("foto3","shorturl.at/abdsP","shorturl.at/abdsP",album);
        foto3.setTitle("foto3");
        foto3.setThumbnailUrl("shorturl.at/abdsP");
        foto3.setUrl("shorturl.at/abdsP");
        foto3.setAlbum(album);
        foto3.setCreatedAt(Instant.now());
        foto3.setUpdatedAt(Instant.now());

        testEntityManager.persist(foto1);
        testEntityManager.persist(foto2);
        testEntityManager.persist(foto3);


        Pageable pageable = PageRequest.of(1, 3, Sort.Direction.DESC, AppConstants.CREATED_AT);


        assertEquals(3, photoRepository.findByAlbumId(album.getId(), pageable).getTotalElements());
    }

    @Test
    void test_findAlbumEmpty(){

        Pageable pageable = PageRequest.of(1, 3, Sort.Direction.DESC, AppConstants.CREATED_AT);

        assertEquals(0, photoRepository.findByAlbumId(any(), pageable).getTotalElements());
    }




}