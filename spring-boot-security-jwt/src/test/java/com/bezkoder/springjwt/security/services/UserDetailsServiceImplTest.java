package com.bezkoder.springjwt.security.services;

import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.bezkoder.springjwt.models.ERole.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;


    @Test
    void loadUserByUsername() {
        User user = new User();
        user.setUsername("test");
        user.setId(123L);
        Set<Role> roles = new HashSet<Role>();
        roles.add(new Role(ROLE_USER));
        user.setRoles(roles);
        user.setEmail("955@gmail.com");
        user.setPassword("12345678");
        userRepository.save(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        UserDetailsImpl userDetailsimpl = (UserDetailsImpl) userDetailsService.loadUserByUsername("test");
        assertEquals(userDetailsimpl.getUsername(),"test");
        assertEquals(userDetailsimpl.getPassword(),"12345678");
        assertEquals(userDetailsimpl.getEmail(),"955@gmail.com");
        assertEquals(userDetailsimpl.getId(),123L);
        assertNotNull(userDetailsimpl.getAuthorities());
        assertTrue(userDetailsimpl.isEnabled());
        assertTrue(userDetailsimpl.isAccountNonExpired());
        assertTrue(userDetailsimpl.isAccountNonLocked());
        assertTrue(userDetailsimpl.isCredentialsNonExpired());
        assertNotNull(userDetailsimpl);
        assertTrue(userDetailsimpl.equals(userDetailsimpl));
    }
}