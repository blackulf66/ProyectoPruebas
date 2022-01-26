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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceimplTest {

    @Mock
    UserRepository userRepository;



    @MockBean
    PostRepository postRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    Role role;





    RoleName roleName;

    @MockBean
    UserPrincipal userPrincipal;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void getUserProfileTest(){

        Company company = new Company();
        company.setName("MacDonald");
        Address address = new Address();
        address.setCity("Sevilla");
        address.setStreet("Calle Condes de Bustillo");



        User user = new User("rafalito");
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


        List <Role> roles = new ArrayList<>();
        RoleName roleName = RoleName.ROLE_USER;
        Role role = new Role();
        role.setName(roleName);


         roles.add(role);



        User user = new User();
        user.setUsername("albertito");
        user.setEmail("albertito@hotmail.com");
        user.setRoles(roles);
        user.setPassword("contrasenia");
        user.setId(1L);








        lenient().when(userRepository.save(user)).thenReturn(user);

        assertEquals(user, userService.addUser(user));


    }



}
