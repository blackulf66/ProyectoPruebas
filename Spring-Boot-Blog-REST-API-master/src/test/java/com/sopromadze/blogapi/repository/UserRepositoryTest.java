package com.sopromadze.blogapi.repository;


import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sopromadze.blogapi.model.role.RoleName.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    User user_prueba;
    UserPrincipal user_principal_prueba;

    @BeforeEach
    void InitData(){

        Role rol = new Role();
        rol.setName(RoleName.ROLE_ADMIN);

        List<Role> roles = Arrays.asList(rol);

        user_prueba = new User("user","de prueba","username12","emailtest@gmail.com","qwerty");
        user_prueba.setRoles(List.of());
        user_prueba.setCreatedAt(Instant.now());
        user_prueba.setUpdatedAt(Instant.now());
        user_prueba.setRoles(roles);
        testEntityManager.persist(user_prueba);

    }

    @Test
    void test_NotNull(){ assertNotNull(userRepository);}

    @Test
    void test_findByUsernameSuccess(){
        assertEquals(Optional.of(user_prueba),userRepository.findByUsername("username12"));
    }

    @Test
    void test_findByEmailSuccess(){
        assertEquals(Optional.of(user_prueba),userRepository.findByEmail("emailtest@gmail.com"));
    }

    @Test
    void test_existsTrueByUsernameSuccess(){
        assertTrue(userRepository.existsByUsername("username12"));
    }

    @Test
    void test_existsFalseByUsernameSuccess(){
        assertFalse(userRepository.existsByUsername("username12312"));
    }

    @Test
    void test_existsTrueByEmailSuccess(){
        assertTrue(userRepository.existsByEmail("emailtest@gmail.com"));
    }

    @Test
    void test_existsFalseByEmailSuccess(){
        assertFalse(userRepository.existsByEmail("emaidsadltest@gmail.com"));
    }

    @Test
    void test_findByUsernameOrEmailSuccess(){
        assertEquals(user_prueba,userRepository.findByUsernameOrEmail("username12","correoinventado@gmail.com").get());
    }

    @Test
    void test_getUserSuccess(){

         user_principal_prueba = UserPrincipal.create(user_prueba);

        assertEquals(user_prueba,userRepository.getUser(user_principal_prueba));
    }

    @Test
    void test_getUserByNameSuccess(){
        assertEquals(user_prueba,userRepository.getUserByName("username12"));

    }
}
