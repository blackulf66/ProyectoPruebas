package com.sopromadze.blogapi.service.impl;


import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomUserDetailsServiceImplTest {

    @InjectMocks
    CustomUserDetailsServiceImpl customUserDetailsService;

    @Mock
    private final ModelMapper modelMapper;
    CustomUserDetailsServiceImplTest() {
        modelMapper = null;
    }

    @Mock
    UserRepository userRepository;

    User user_prueba;

    @BeforeEach
    void init_data(){
        Role rol = new Role();
        rol.setName(RoleName.ROLE_ADMIN);

        user_prueba = new User("user","de prueba","username12","emailtest@gmail.com","qwerty");
        user_prueba.setRoles(List.of(rol));
    }

    @Test
    void test_loadUserByUsernameSuccess(){

        when(userRepository.save(user_prueba)).thenReturn(user_prueba);
        when(userRepository.findByUsernameOrEmail("username12","emailtest@gmail.com")).thenReturn(Optional.of(user_prueba));

        assertEquals(UserPrincipal.create(user_prueba),customUserDetailsService.loadUserByUsername("emailtest@gmail.com"));

    }

}
