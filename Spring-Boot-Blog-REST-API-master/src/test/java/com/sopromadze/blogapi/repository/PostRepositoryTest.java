package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.config.AuditingConfig;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.config.TestAuditingConfig;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

//TODO Hecho por Alfonso Gallardo
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = ASSIGNABLE_TYPE,
        classes = {AuditorAware.class, TestAuditingConfig.class}
))
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({SpringSecurityTestWebConfig.class})
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ApplicationContext applicationContext;

    Post post;
    Category category;
    Tag tag;
    Pageable pageable;
    User user;

    @BeforeEach
    void init() {

        user = new User();
        user.setEmail("alfonsogallardo@email.com");
        user.setFirstName("Alfonso");
        user.setUsername("Alfonsogr");
        user.setLastName("Gallardo");
        user.setPassword("password");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        category = new Category();
        category.setName("Nueva Categoría");
        category.setCreatedBy(user.getId());
        category.setCreatedAt(Instant.now());
        category.setUpdatedAt(Instant.now());

        tag = new Tag();
        tag.setCreatedAt(Instant.now());
        tag.setUpdatedAt(Instant.now());
        tag.setCreatedBy(user.getId());
        tag.setName("Nuevo Tag");

        post = new Post();
        post.setTitle("Nuevo post");
        post.setBody("El perro de san roque no tiene rabo por que Ramón Ramírez se lo ha cortado");
        post.setUser(user);
        post.setCategory(category);
        post.setCreatedBy(user.getId());
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());
        post.setCreatedBy(user.getId());


        testEntityManager.persist(user);
        testEntityManager.persist(category);
        testEntityManager.persist(tag);
        testEntityManager.persist(post);


        pageable = PageRequest.of(0,1, Sort.Direction.DESC, CREATED_AT);

        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = "customUserDetailsService")
    void test_findByCreatedBy () {



        Page<Post> result = postRepository.findByCreatedBy(user.getId(), pageable);
        assertTrue(result.getTotalElements()!=0);
    }

    @Test
    void test_findByCategory() {

        Page<Post> result = postRepository.findByCategoryId(user.getId(), pageable);
        assertTrue(result.getTotalElements()!=0);
    }


    @Test
    void test_findByTags() {
        Page<Post> result = postRepository.findByTagsIn(Collections.singletonList(tag), pageable);
        assertTrue(result.getTotalElements()!=0);

    }
}