package com.bezkoder.springjwt.security.jwt;

import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.WebSecurityConfig;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import com.bezkoder.springjwt.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.AccessType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.bezkoder.springjwt.models.ERole.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder encoder;

    static JwtUtils jwtUtils = new JwtUtils();

    static User user = new User();

    static Authentication auth = Mockito.mock(Authentication.class);

    static UserDetailsImpl userDetailsimpl;

    @BeforeAll
    static void init(){
        user.setUsername("test");
        user.setId(123L);
        Set<Role> roles = new HashSet<Role>();
        roles.add(new Role(ROLE_USER));
        user.setRoles(roles);
        user.setEmail("955@gmail.com");
        user.setPassword("12345678");
        userDetailsimpl = UserDetailsImpl.build(user);
        when(auth.getPrincipal()).thenReturn(userDetailsimpl);
        jwtUtils.setJwtExpirationMs(86400000);
        jwtUtils.setJwtSecret("secret");
    }


    @Test
    void generateJwtToken() {

        when(auth.getPrincipal()).thenReturn(userDetailsimpl);
        String s = jwtUtils.generateJwtToken(auth);
        assertNotNull(s);
    }

    @Test
    void getUserNameFromJwtToken() {

        String s = jwtUtils.generateJwtToken(auth);
        assertEquals(jwtUtils.getUserNameFromJwtToken(jwtUtils.generateJwtToken(auth)),"test");

    }

    @Test
    void validateJwtToken() {
        String s = jwtUtils.generateJwtToken(auth);
        assertTrue(jwtUtils.validateJwtToken(s));

    }
}