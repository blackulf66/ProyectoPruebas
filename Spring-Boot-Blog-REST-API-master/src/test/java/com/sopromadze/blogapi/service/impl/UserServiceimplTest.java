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
import java.util.stream.Collectors;

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

        userRepository.save(user);

        assertTrue(this.userRepository.existsByUsername("albertito"));

        when(this.userRepository.findByEmail("albertito@hotmail.com")).thenReturn(Optional.of(user));

        userRepository.save(user);

        assertTrue(this.userRepository.existsByUsername("albertito@hotmail.com"));



    }

    @Test
    void giveAdminTestSuccessThrowAppException(){


        Address address = new Address();
        address.setCity("Sevilla");
        address.setStreet("Calle Condes de Bustillo");
        Company company = new Company();
        company.setName("MacDonald");



        List<Role> roles = new ArrayList<>();

        Role roleUser = new Role();
        roleUser.setName(RoleName.ROLE_USER);
        Role roleAdmin = new Role();
        roleAdmin.setName(RoleName.ROLE_ADMIN);
        List<Role> rolesUser = new ArrayList<>();

        rolesUser.add(roleUser);


        User usuario = new User();
        usuario.setId(1L);
        usuario.setFirstName("manuel");
        usuario.setUsername("manspitub");
        usuario.setLastName("spinola");
        usuario.setEmail("manspitub@@hotmail.com");
        usuario.setAddress(address);
        usuario.setCompany(company);
        usuario.setPhone("459403477");
        usuario.setRoles(rolesUser);

        roles.add(roleAdmin);
        roles.add(roleUser);

        User userWithoutRole = new User();
        userWithoutRole.setId(2L);
        userWithoutRole.setFirstName("manuel");
        userWithoutRole.setUsername("manuelitoo");
        userWithoutRole.setLastName("spinola");
        userWithoutRole.setEmail("manspitub@@hotmail.com");
        userWithoutRole.setAddress(address);
        userWithoutRole.setCompany(company);
        userWithoutRole.setPhone("459403477");

        userRepository.save(userWithoutRole);

        assertThrows(AppException.class, ()-> service.giveAdmin("manuelitoo"));

















    }

    @Test
    void removeAdminFromUserTest(){
        Address address = new Address();
        address.setCity("Sevilla");
        address.setStreet("Calle Condes de Bustillo");
        Company company = new Company();
        company.setName("MacDonald");



        List<Role> roles = new ArrayList<>();

        Role roleUser = new Role();
        roleUser.setName(RoleName.ROLE_USER);
        Role roleAdmin = new Role();
        roleAdmin.setName(RoleName.ROLE_ADMIN);
        List<Role> rolesUser = new ArrayList<>();

        roles.add(roleAdmin);


        User usuario = new User();
        usuario.setId(1L);
        usuario.setFirstName("manuel");
        usuario.setUsername("manspitub");
        usuario.setLastName("spinola");
        usuario.setEmail("manspitub@@hotmail.com");
        usuario.setAddress(address);
        usuario.setCompany(company);
        usuario.setPhone("459403477");
        usuario.setRoles(roles);

        roles.add(roleAdmin);
        roles.add(roleUser);

        Role roleAdmin1 = new Role();

        roleAdmin1.setName(RoleName.ROLE_ADMIN);




        userRepository.save(usuario);

        service.removeAdmin("manspitub");

        assertFalse(usuario.getRoles().contains(roleAdmin1));

    }



}
