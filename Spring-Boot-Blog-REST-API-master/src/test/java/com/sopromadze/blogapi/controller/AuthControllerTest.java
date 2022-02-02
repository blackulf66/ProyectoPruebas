package com.sopromadze.blogapi.controller;

import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.LoginRequest;
import com.sopromadze.blogapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
public class AuthControllerTest {

    @Mock
    private UserService userService;

    private Mock mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void itShouldntAuthenticate()  {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("pepito");
        loginRequest.setPassword("heyluismi");

        User user = new User();
        user.setUsername("pep");

        when(userService.addUser(user)).thenReturn().


    }

}
