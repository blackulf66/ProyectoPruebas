package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    Post post;
    Category category;
    Tag tag;
    Pageable pageable;
    User user;

    @BeforeEach
    void init() {
        category = new Category();
        category.setName("Nueva Categoría");
        category.setCreatedAt(Instant.now());
        category.setUpdatedAt(Instant.now());

        user = new User();
        user.setEmail("alfonsogallardo@email.com");
        user.setFirstName("Alfonso");
        user.setUsername("Alfonsogr");
        user.setLastName("Gallardo");
        user.setPassword("password");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        tag = new Tag();
        tag.setName("Nuevo tag");
        tag.setCreatedAt(Instant.now());
        tag.setUpdatedAt(Instant.now());

        post = new Post();
        post.setTitle("Nuevo post");
        post.setBody("El perro de san roque no tiene rabo por que Ramón Ramírez se lo ha cortado");
        post.setCategory(category);
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        testEntityManager.persist(post);
        testEntityManager.persist(category);
        testEntityManager.persist(tag);
        testEntityManager.persist(user);

        pageable = PageRequest.of(1,1, Sort.Direction.DESC, CREATED_AT);
    }

    @Test
    void test_findByCreatedBy () {
        Page<Post> result = postRepository.findByCreatedBy(user.getId(), pageable);
        assertTrue(result.getTotalElements()!=0);
    }

    @Test
    void test_findByCategory() {

        Page<Post> result = postRepository.findByCategory(user.getId(), pageable);
        assertTrue(result.getTotalElements()!=0);
    }


    @Test
    void test_findByTags() {
        Page<Post> result = postRepository.findByTagsIn(Collections.singletonList(tag), pageable);

        assertTrue(result.getTotalElements()!=0);

    }
}