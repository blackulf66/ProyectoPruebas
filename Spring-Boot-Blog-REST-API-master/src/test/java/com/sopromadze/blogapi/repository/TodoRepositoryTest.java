package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Todo;
import com.sopromadze.blogapi.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;


import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach

    void init() {
        User user = new User();
        user.setEmail("user@email.com");
        user.setFirstName("FirstName");
        user.setUsername("UserName");
        user.setLastName("LastName");
        user.setPassword("password");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        testEntityManager.persist(user);

        Todo todo= new Todo();
        todo.setTitle("Todo");
        todo.setCreatedAt(Instant.now());
        todo.setUpdatedAt(Instant.now());
        todo.setUser(user);


        testEntityManager.persist(todo);
    }
    @Test
    void test_NotNull(){
        assertNotNull(todoRepository);
    }

    @Test
    void test_todoRepository () {

        Page<Todo> result = todoRepository.findByCreatedBy(1L, PageRequest.of(1,10));
        assertTrue(result.getTotalElements()!=0);
        //lenient().when(todoRepository.findByCreatedBy(user.getId(), pageRequest)).thenReturn(todos);
        //assertNotEquals(0, todoRepository.findByCreatedBy(1L, pageRequest).getTotalElements());

    }

}