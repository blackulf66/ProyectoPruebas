package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.AppException;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.Address;
import com.sopromadze.blogapi.model.user.Company;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.UserProfile;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.RoleRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.apache.logging.log4j.ThreadContext.isEmpty;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    UserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;





    @MockBean
    PostRepository postRepository;

    @Mock
    RoleRepository roleRepository;

    @MockBean
    Role role;





    RoleName roleName;

    @MockBean
    UserPrincipal userPrincipal;

    @InjectMocks
    UserServiceImpl service;

    @Test
    void getUserProfileTest(){

        Company company = new Company();
        company.setName("MacDonald");
        Address address = new Address();
        address.setCity("Sevilla");
        address.setStreet("Calle Condes de Bustillo");

        List<Post> posts = new ArrayList<>();
        Post post = new Post();
        post.setId(3L);
        post.setTitle("Post");
        post.setBody("Cuerpo");
        posts.add(post);



        User user = new User();
        user.setId(8L);
        user.setFirstName("rafael");
        user.setLastName("Perez");
        user.setPhone("348936200");
        user.setCompany(company);
        user.setAddress(address);
        user.setCreatedAt(Instant.now());
        user.setWebsite("salesianos.com");
        user.setEmail("rafalito@gmail.com");
        user.setPosts(posts);

        when(userRepository.getUserByName("rafael")).thenReturn(user);
        assertEquals(user, userRepository.getUserByName("rafael"));











    }

    @Test
    void whenNewUser_success(){

        Company company = new Company();
        company.setName("MacDonald");
        Address address = new Address();
        address.setCity("Sevilla");
        address.setStreet("Calle Condes de Bustillo");

        List<Post> posts = new ArrayList<>();
        Post post = new Post();
        post.setId(3L);
        post.setTitle("Post");
        post.setBody("Cuerpo");
        posts.add(post);


        Role role = new Role();


        List <Role> roles = new ArrayList<>();

        role.setName(RoleName.ROLE_USER);
        role.setId(1L);



        User user = new User();
        user.setId(8L);
        user.setFirstName("rafael");
        user.setUsername("rafalito");
        user.setLastName("Perez");
        user.setPhone("348936200");
        user.setCompany(company);
        user.setAddress(address);
        user.setCreatedAt(Instant.now());
        user.setWebsite("salesianos.com");
        user.setEmail("rafalito@gmail.com");
        user.setPosts(posts);
        user.setRoles(roles);

        when(userService.addUser(user)).thenReturn(user);
        assertEquals(user, userService.addUser(user));


    }

    @Test
    @Transactional
    void shouldUpdateUser(){
        Company company = new Company();
        company.setName("MacDonald");
        Address address = new Address();
        address.setCity("Sevilla");
        address.setStreet("Calle Condes de Bustillo");

        List<Post> posts = new ArrayList<>();
        Post post = new Post();
        post.setId(3L);
        post.setTitle("Post");
        post.setBody("Cuerpo");
        posts.add(post);


        Role role = new Role();


        List <Role> roles = new ArrayList<>();

        role.setName(RoleName.ROLE_ADMIN);
        role.setId(1L);
        roles.add(role);

        Role role1 = new Role();
        List<Role> roles1 = new ArrayList<>();
        role1.setName(RoleName.ROLE_USER);
        role.setId(2L);
        roles1.add(role1);



        User user = new User();
        user.setId(8L);
        user.setFirstName("rafael");
        user.setUsername("rafalito");
        user.setLastName("Perez");
        user.setPhone("348936200");
        user.setCompany(company);
        user.setAddress(address);
        user.setCreatedAt(Instant.now());
        user.setWebsite("salesianos.com");
        user.setEmail("rafalito@gmail.com");
        user.setPosts(posts);
        user.setRoles(roles);

        String oldFirstName = user.getFirstName();
        String newFirstName = oldFirstName + "o";

        user.setFirstName(newFirstName);

        assertThat(user.getFirstName()).isEqualTo("rafaelo");

        User userAdmin = new User();
        userAdmin.setRoles(roles);

        assertThat(userAdmin.getRoles().get(0).getName()).isEqualTo(RoleName.ROLE_ADMIN);

        User anotherUser = new User();
        anotherUser.setRoles(roles1);
        anotherUser.setUsername("manolito");
        UserPrincipal userPrueba = mock(UserPrincipal.class);








    }



    @Test
    void giveAdminTestExceptionSuccess(){


        Company company = new Company();
        company.setName("MacDonald");
        Address address = new Address();
        address.setCity("Sevilla");
        address.setStreet("Calle Condes de Bustillo");

        List<Post> posts = new ArrayList<>();
        Post post = new Post();
        post.setId(3L);
        post.setTitle("Post");
        post.setBody("Cuerpo");
        posts.add(post);


        Role role = new Role();


        List <Role> roles = new ArrayList<>();

        role.setName(RoleName.ROLE_ADMIN);
        role.setId(1L);
        roles.add(role);

        Role role1 = new Role();
        List<Role> roles1 = new ArrayList<>();
        role1.setName(RoleName.ROLE_USER);
        role.setId(2L);
        roles1.add(role1);



        User user = new User();
        user.setId(8L);
        user.setFirstName("rafael");
        user.setUsername("rafalito");
        user.setLastName("Perez");
        user.setPhone("348936200");
        user.setCompany(company);
        user.setAddress(address);
        user.setCreatedAt(Instant.now());
        user.setWebsite("salesianos.com");
        user.setEmail("rafalito@gmail.com");
        user.setPosts(posts);
        user.setRoles(roles);








    }





}