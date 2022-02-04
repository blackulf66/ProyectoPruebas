package com.sopromadze.blogapi.controller;

import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.*;
import com.sopromadze.blogapi.security.JwtTokenProvider;
import com.sopromadze.blogapi.service.UserService;
import lombok.extern.java.Log;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.function.ServerResponse;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Log
@WebMvcTest

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
public class AuthControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Authentication authentication;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthControllerTest(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Disabled("HAS ERRORS")
    @WithMockUser(roles = "ADMIN", password = "1234", username = "pep")
     void authRequest_shouldSucceedWith200() throws Exception{

        String jwt = jwtTokenProvider.generateToken(authentication);


        mockMvc.perform(MockMvcRequestBuilders.post("signin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
    }



}
