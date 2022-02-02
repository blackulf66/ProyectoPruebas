package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.Tag;
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
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void test_NotNull(){ assertNotNull(tagRepository);}

    @Test
        //TEST: test para comprobar que hay objetos en el repositorio
        //ENTRADA:
        //RESULTADO ESPERADO: devolver los elementos totales del repositorio
    void test_commentRepository_success() {

        Tag tag = new Tag();
        tag.setName("Selina Kyle");
        tag.setCreatedBy(1L);
        tag.setUpdatedAt(Instant.now());
        tag.setCreatedAt(Instant.now());

        testEntityManager.persist(tag);

        assertEquals(1, tagRepository.findByName(tag.getName()).getId());


    }




}