package com.sopromadze.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.UserIdentityAvailability;
import com.sopromadze.blogapi.payload.UserProfile;
import com.sopromadze.blogapi.payload.UserSummary;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.CommentService;
import com.sopromadze.blogapi.service.UserService;
import com.sopromadze.blogapi.utils.AppUtils;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.modelmapper.internal.bytebuddy.matcher.ElementMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AppUtils appUtils;

    User usuario_test;

    @BeforeEach
    void InitData(){

        usuario_test = new User("usuario","de prueba","username","testemail@gmail.com","qwerty");
        usuario_test.setId(1L);
        usuario_test.setRoles(List.of(new Role(RoleName.ROLE_ADMIN)));
    }

    @Test
    @DisplayName("GET /api/users/me")
    @WithMockUser(username = "userMock", password = "pwd", roles = "USER")
    void test_getCurrentUserSueccess() throws Exception {


        UserPrincipal userPrincipal = UserPrincipal.create(usuario_test);

        UserSummary result = new UserSummary(usuario_test.getId(), usuario_test.getUsername(),
                usuario_test.getFirstName(),usuario_test.getLastName());

        when(userService.getCurrentUser(userPrincipal)).thenReturn(result);

        mockMvc.perform(get("/api/users/me")
                        .contentType("application/json"))
                .andExpect(status().isOk()).andDo(print());

    }

    @Test
    @DisplayName("GET /api/users/checkUsernameAvailability")
    void test_checkUsernameAvailabilitySuccess() throws Exception{

        UserIdentityAvailability noDisponible = new UserIdentityAvailability(false);

        when(userService.checkUsernameAvailability(usuario_test.getUsername())).thenReturn(noDisponible);

        mockMvc.perform(get("/api/users/checkUsernameAvailability")
                        .param("username", usuario_test.getUsername())
                        .contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(noDisponible)))
                .andExpect(status().isOk()).andDo(print());

    }

    @Test
    @DisplayName("GET /api/users/checkEmailAvailability")
    void test_checkEmailAvailabilitySuccess() throws Exception{

        UserIdentityAvailability noDisponible = new UserIdentityAvailability(false);

        when(userService.checkEmailAvailability(usuario_test.getEmail())).thenReturn(noDisponible);

        mockMvc.perform(get("/api/users/checkEmailAvailability")
                        .param("email", usuario_test.getEmail())
                        .contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(noDisponible)))
                .andExpect(status().isOk()).andDo(print());

    }

    @Test
    @DisplayName("GET /api/users/{username}/profile")
    void test_getUSerProfileSuccess() throws Exception {

        UserProfile userProfile = new UserProfile(usuario_test.getId(),usuario_test.getUsername(),
                usuario_test.getFirstName(),usuario_test.getLastName(), Instant.now(),
                usuario_test.getEmail(),usuario_test.getAddress(), usuario_test.getPhone(),
                usuario_test.getWebsite(),usuario_test.getCompany(),
                0L);

        when(userService.getUserProfile(usuario_test.getUsername())).thenReturn(userProfile);
        mockMvc.perform(get("/api/users/{username}/profile",usuario_test.getUsername())
                        .contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(userProfile)))
                .andExpect(status().isOk()).andDo(print());
    }

}
