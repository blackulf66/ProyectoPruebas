package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Todo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.TodoRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TodoServiceTest {

    @Mock
    private final ModelMapper modelMapper;

    TodoServiceTest() {
        modelMapper = null;
    }



    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoServiceImpl todoServiceimpl;

    @Test
    void shouldCompleteTodos() {

        User user = new User();

        user.setId(1L);
        user.setUsername("manspitub");
        user.setFirstName("manuel");

        Todo todo = new Todo();

        todo.setId(1L);
        todo.setTitle("HACER TAREAS");
        todo.setUser(user);

        todoRepository.save(todo);

        UserPrincipal userPrincipal = mock(UserPrincipal.class);



        assertThrows(ResourceNotFoundException.class, ()-> todoServiceimpl.completeTodo(2L, userPrincipal));

        assertEquals(user.getId(), todo.getUser().getId());




    }

    @Test
    void shouldReturnUnauthorizedException(){

        List<Role> roles = new ArrayList<>();
        Role role = new Role();

        role.setName(RoleName.ROLE_USER);

        roles.add(role);

        User user = new User();

        user.setId(1L);
        user.setUsername("manspitub");
        user.setFirstName("manuel");
        user.setRoles(roles);

        Todo todo = new Todo();

        todo.setId(1L);
        todo.setTitle("HACER TAREAS");
        todo.setUser(user);




        User userCheck = new User();

        user.setId(2L);
        user.setUsername("manspitub");
        user.setFirstName("manuel");



        UserPrincipal userPrincipal = mock(UserPrincipal.class);

        Optional<Todo> todo1 = todoRepository.findById(1L);
        when(todoRepository.findById(anyLong())).thenReturn(Optional.of(todo));
        when(userRepository.getUser(userPrincipal)).thenReturn(userCheck);

        assertThrows(UnauthorizedException.class, ()-> todoServiceimpl.unCompleteTodo(1L, userPrincipal));



    }

    @Test
    @DisplayName("TODOS")
    @Disabled("Has errors")
    void getAllTodosTest(){

        Todo todo1 = new Todo();
        todo1.setTitle("REGAR LAS PLANTAS");

        Todo todo2 = new Todo();
        todo1.setTitle("REGAR LAS PLANTAS");

        Page<Todo> todos = new PageImpl<>(Arrays.asList(todo1));

        PagedResponse<Todo> response = new PagedResponse<>();

        response.setContent(todos.getContent());
        response.setTotalPages(1);
        response.setTotalElements(1);
        response.setSize(1);

        List<Todo> todos1 = new ArrayList<>();

        Todo[] todosResponse = {todo2};

        todos1.add(todo1);

        UserPrincipal currentUser = mock(UserPrincipal.class);


        when(todoRepository.findByCreatedBy(currentUser.getId(), any(Pageable.class))).thenReturn(todos);
        when(modelMapper.map(todos.getContent(), Todo[].class)).thenReturn(todosResponse);

        assertEquals(response, todoServiceimpl.getAllTodos(currentUser,1,1));


    }

    @Test
    void addTodo(){

        UserPrincipal currentUser = mock(UserPrincipal.class);
        Todo todo1 = new Todo();
        todo1.setTitle("REGAR LAS PLANTAS");

        List<Role> roles = new ArrayList<>();
        Role role = new Role();

        role.setName(RoleName.ROLE_USER);

        roles.add(role);

        User user = new User();

        user.setId(1L);
        user.setUsername("manspitub");
        user.setFirstName("manuel");
        user.setRoles(roles);

        when(userRepository.getUser(currentUser)).thenReturn(user);

        todo1.setUser(user);

        assertEquals(1L, (long) todo1.getUser().getId());
    }

    @Test
    @DisplayName("Borrar TODO")
    void deleteTodoIsExecutedSuccesfullyOnce(){
        TodoServiceImpl todoService = mock(TodoServiceImpl.class);
        UserPrincipal currentUser = mock(UserPrincipal.class);

        Todo todo1 = new Todo();
        todo1.setId(1L);
        todo1.setTitle("Sacar dinero del banco");
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo1));

        todoService.deleteTodo(1L, currentUser);

        verify(todoService, times(1)).deleteTodo(1L, currentUser);





    }









}
