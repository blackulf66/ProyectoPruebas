package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void test_NotNull(){ assertNotNull(commentRepository);}

    @Test
    void test_commentRepositorySuccess() {

        Post post = new Post();
        post.setBody("sdfsadsad");
        post.setUpdatedAt(Instant.now());
        post.setCreatedAt(Instant.now());


        testEntityManager.persist(post);

        Comment comment = new Comment();
        comment.setName("felicia hardy");
        comment.setEmail("elemail@gmail.com");
        comment.setBody("dsfghjidfsoihusdfo");
        comment.setUpdatedAt(Instant.now());
        comment.setCreatedAt(Instant.now());
        comment.setPost(post);

        testEntityManager.persist(comment);

        assertEquals(List.of(comment), commentRepository.findByPostId(post.getId(), any(PageRequest.class)).getContent());


    }

    @Test
    void test_commentRepositoryFail() {

        Post post = new Post();
        post.setBody("sdfsadsad");
        post.setUpdatedAt(Instant.now());
        post.setCreatedAt(Instant.now());


        testEntityManager.persist(post);

        Comment comment = new Comment();
        comment.setName("felicia hardy");
        comment.setEmail("elemail@gmail.com");
        comment.setBody("dsfghjidfsoihusdfo");
        comment.setUpdatedAt(Instant.now());
        comment.setCreatedAt(Instant.now());
        comment.setPost(post);

        testEntityManager.persist(comment);

        assertNotEquals(List.of(comment), commentRepository.findByPostId(post.getId(), any(PageRequest.class)).getContent());



    }

}