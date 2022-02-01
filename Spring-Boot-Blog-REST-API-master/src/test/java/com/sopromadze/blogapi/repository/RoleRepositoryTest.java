package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void test_repositoryNotNull () {
        assertNotNull(roleRepository);
    }

    @Test
    void test_findByName() {
        Role role = new Role();
        role.setName(RoleName.ROLE_ADMIN);

        testEntityManager.persist(role);

        assertEquals(role, roleRepository.findByName(RoleName.ROLE_ADMIN).get());
    }

    @Test
    void test_findByNameException() {
        assertThrows(NoSuchElementException.class, () -> roleRepository.findByName(RoleName.ROLE_ADMIN).get());
    }

}