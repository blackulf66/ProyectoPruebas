package com.sopromadze.blogapi.controller;

import com.sopromadze.blogapi.config.SpringSecurityTestWebConfig;
import lombok.extern.java.Log;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestWebConfig.class})
@AutoConfigureMockMvc
public class UserController {
}
