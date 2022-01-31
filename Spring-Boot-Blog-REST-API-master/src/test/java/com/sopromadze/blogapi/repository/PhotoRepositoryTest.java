package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
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
    void test_commentRepository_success() {

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
    void test_commentRepository_fail() {

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

        assertNotEquals(0, albumRepository.findByCreatedBy(album.getId(), pageRequest).getTotalElements());

    }




}