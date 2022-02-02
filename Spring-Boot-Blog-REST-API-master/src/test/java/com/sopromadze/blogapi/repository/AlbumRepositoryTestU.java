package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.user.User;
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
class AlbumRepositoryTestU {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
        //TEST: test de repositorio no nulo
        //ENTRADA: assertNotNull(albumRepository)
        //RESULTADO ESPERADO: que devuelva not null
    void test_NotNull(){ assertNotNull(albumRepository);}

    @Test
        //TEST: test para comprobar que hay objetos en el repositorio
        //ENTRADA:
        //RESULTADO ESPERADO: devolver los elementos totales del repositorio
    void test_albumRepository_success(){

        Album album = new Album();
        album.setTitle("flying grayson");
        album.setCreatedBy(1L);
        album.setUpdatedAt(Instant.now());
        album.setCreatedAt(Instant.now());

        testEntityManager.persist(album);

        Page<Album> albums = new PageImpl<>(Arrays.asList(album));
        PageRequest pageRequest = PageRequest.of(1, 1);

        assertEquals(1, albumRepository.findByCreatedBy(album.getCreatedBy(), pageRequest).getTotalElements());

    }




}