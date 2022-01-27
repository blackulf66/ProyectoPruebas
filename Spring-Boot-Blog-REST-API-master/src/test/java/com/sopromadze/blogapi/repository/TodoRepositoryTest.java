package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.Todo;
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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;



    @Test
    void test_NotNull(){
        assertNotNull(todoRepository);
    }

    @Test
    void test_todoRepository () {
        User user = new User();
        user.setId(1L);
        user.setUsername("Alfonso");

        Todo todo = new Todo();
        todo.setUser(user);

        Page<Todo> result = todoRepository.findByUserId(1L, PageRequest.of(1,10));
        assertTrue(result.getTotalElements()!=0);
        //lenient().when(todoRepository.findByCreatedBy(user.getId(), pageRequest)).thenReturn(todos);
        //assertNotEquals(0, todoRepository.findByCreatedBy(1L, pageRequest).getTotalElements());

    }

}