package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.user.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import javax.swing.text.html.parser.Entity;
import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTestULF {

    @Autowired
    private CommentRepository CommentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void test_NotNull(){ assertNotNull(CommentRepository);}

    @Test
    void test_commentRepository_success() {

        Comment comment = new Comment();
        comment.setName("felicia hardy");
        comment.setEmail("elemail@gmail.com");
        comment.setBody("dsfghjidfsoihusdfo");
        comment.setUpdatedAt(Instant.now());
        comment.setCreatedAt(Instant.now());

        testEntityManager.persist(comment);

        Post post = new Post();
        post.setBody("sdfsadsad");
        post.setUpdatedAt(Instant.now());
        post.setCreatedAt(Instant.now());


        testEntityManager.persist(post);

        Page<Comment> comments = new PageImpl<>(Arrays.asList(comment));
        PageRequest pageRequest = PageRequest.of(1, 2);

        assertEquals(0, CommentRepository.findByPostId(post.getId(), pageRequest).getTotalElements());

    }

    @Test
    void test_commentRepository_fail() {

        Comment comment = new Comment();
        comment.setName("felicia hardy");
        comment.setEmail("elemail@gmail.com");
        comment.setBody("dsfghjidfsoihusdfo");
        comment.setUpdatedAt(Instant.now());
        comment.setCreatedAt(Instant.now());

        testEntityManager.persist(comment);

        Post post = new Post();
        post.setBody("sdfsadsad");
        post.setUpdatedAt(Instant.now());
        post.setCreatedAt(Instant.now());


        testEntityManager.persist(post);

        Page<Comment> comments = new PageImpl<>(Arrays.asList(comment));
        PageRequest pageRequest = PageRequest.of(1, 2);

        assertNotEquals(0, CommentRepository.findByPostId(post.getId(), pageRequest).getTotalElements());

    }

}