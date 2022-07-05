package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.repository.UserRepository;
import com.mysql.cj.log.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static com.bezkoder.springjwt.models.ERole.ROLE_USER;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    UserRepository userRepository;

    @Mock
    AuthController authController;

    @Autowired
    private MockMvc mockMvc;

    LoginRequest loginRequest = new LoginRequest();

    @BeforeAll
    public void init(){
        List<String> roles = new ArrayList<>();
        roles.add("new Role(ROLE_USER)");
        when(authController.authenticateUser(loginRequest))
//                .thenReturn(ResponseEntity.ok(new JwtResponse("JWT",
//                        1L,
//                        "admin",
//                        "userDetails.getEmail()",
//                        roles)));
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }

    @Test
    public void LoginTest(){
        LoginRequest loginRequest = new LoginRequest();
        mockMvc.perform()
    }
}
