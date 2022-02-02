package com.sopromadze.blogapi.repository;


import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Todo;
import com.sopromadze.blogapi.model.user.User;
import org.hibernate.mapping.Any;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void test_NotNull(){ assertNotNull(todoRepository); }

    @Test
    void test_findByCreatedBySuccess(){

        User user_prueba = new User("user","de prueba","username12","emailtest@gmail.com","qwerty");
        user_prueba.setCreatedAt(Instant.now());
        user_prueba.setUpdatedAt(Instant.now());

        testEntityManager.persist(user_prueba);

        Todo todo = new Todo();
        todo.setTitle("Título todo");
        todo.setUser(user_prueba);
        todo.setCreatedBy(user_prueba.getId());
        todo.setUpdatedBy(user_prueba.getId());
        todo.setUpdatedAt(Instant.now());
        todo.setCreatedAt(Instant.now());

        testEntityManager.persist(todo);

        assertEquals(List.of(todo), todoRepository.findByCreatedBy(user_prueba.getId(), any(PageRequest.class)).getContent());

    }
    

}
