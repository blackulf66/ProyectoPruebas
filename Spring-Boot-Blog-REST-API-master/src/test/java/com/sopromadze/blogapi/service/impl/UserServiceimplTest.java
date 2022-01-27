package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.AppException;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.Address;
import com.sopromadze.blogapi.model.user.Company;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.UserProfile;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.RoleRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
import static org.mockito.Mockito.mock;
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceimplTest {

    @Mock
    UserRepository userRepository;

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

        userRepository.save(user);


        Long postCount = postRepository.countByCreatedBy(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getCreatedAt(), user.getEmail(), user.getAddress(), user.getPhone(), user.getWebsite(),
                user.getCompany(), postCount
);


        when(userRepository.getUserByName("rafaelito")).thenReturn(user);
        assertEquals(userProfile, userService.getUserProfile(user.getUsername()));



    }

    @Test
    void whenNewUser_success(){




        Role role = new Role();
        User user = new User();
        user.setId((1L));
        user.setUsername("albertito");
        user.setEmail("albertito@hotmail.com");

        List <Role> roles = new ArrayList<>();

        role.setName(RoleName.ROLE_USER);
        role.setId(1L);













        when(this.userRepository.findByUsername("albertito")).thenReturn(Optional.of(user));

        Optional<User> user1 = this.userRepository.findByUsername("albertito");
        assertEquals(0, userRepository.count());


      // User user1 = this.service.addUser(user);

        //assertNotNull(user1);
        //assertEquals(1L, user1.getId());


    }

    @Test
    void giveAdminTestSuccess(){


        Address address = new Address();
        address.setCity("Sevilla");
        address.setStreet("Calle Condes de Bustillo");
        Company company = new Company();
        company.setName("MacDonald");

        User usuario = new User();
        usuario.setFirstName("manuel");
        usuario.setUsername("manspitub");
        usuario.setLastName("spinola");
        usuario.setEmail("manspitub@@hotmail.com");
        usuario.setAddress(address);
        usuario.setCompany(company);
        usuario.setPhone("459403477");

    }

    @Test
    void checkThis(){
        String palabra = "ey que ase";

        assertTrue(palabra.length() ==10);
        if (palabra.length() ==10 )
            System.out.println("es verdadero");
    }



}
