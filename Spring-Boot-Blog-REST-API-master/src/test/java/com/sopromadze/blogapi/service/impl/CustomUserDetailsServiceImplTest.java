package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.UserService;

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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CustomUserDetailsServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsServiceImpl customUserDetailsService;


    /*
    * @Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) {
		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with this username or email: %s", usernameOrEmail)));
		return UserPrincipal.create(user);
	}
	*/
    @Test
    void test1() {

        Role role = new Role(RoleName.ROLE_ADMIN);

        List<Role> listaRoles = Arrays.asList(role);

        User user = new User();
        user.setId(1L);
        user.setFirstName("Damian Wayne");
        user.setUsername("bestRobin");
        user.setEmail("robin@gothammail.com");
        user.setRoles(listaRoles);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        when(userRepository.findByUsernameOrEmail(any(),any())).thenReturn(java.util.Optional.of(user));

        assertEquals(userPrincipal, customUserDetailsService.loadUserByUsername("bestRobin"));


    }

    @Test
    void exception() {

        Role role = new Role(RoleName.ROLE_ADMIN);

        List<Role> listaRoles = Arrays.asList(role);

        User user = new User();
        user.setId(1L);
        user.setFirstName("Damian Wayne");
        user.setUsername("bestRobin");
        user.setEmail("robin@gothammail.com");
        user.setRoles(listaRoles);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        when(userRepository.findByUsernameOrEmail("batman","batman@gothammail.com")).thenReturn(java.util.Optional.of(user));

        assertThrows(UsernameNotFoundException.class,()-> customUserDetailsService.loadUserByUsername("bestRobin"));


    }
    /*
	@Override
	@Transactional
	public UserDetails loadUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with id: %s", id)));

		return UserPrincipal.create(user);
	}*/

    @Test
    void testasdredafs() {

        Role role = new Role(RoleName.ROLE_ADMIN);

        List<Role> listaRoles = Arrays.asList(role);

        User user = new User();
        user.setId(1L);
        user.setFirstName("Damian Wayne");
        user.setUsername("bestRobin");
        user.setEmail("robin@gothammail.com");
        user.setRoles(listaRoles);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));

        assertEquals(userPrincipal, customUserDetailsService.loadUserById(1L));


    }
    @Test
    void testasdrdadsedafs() {

        Role role = new Role(RoleName.ROLE_ADMIN);

        List<Role> listaRoles = Arrays.asList(role);

        User user = new User();
        user.setId(1L);
        user.setFirstName("Damian Wayne");
        user.setUsername("bestRobin");
        user.setEmail("robin@gothammail.com");
        user.setRoles(listaRoles);

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        when(userRepository.findById(14L)).thenReturn(java.util.Optional.of(user));

        assertThrows(UsernameNotFoundException.class,()-> customUserDetailsService.loadUserById(1L));


    }



}