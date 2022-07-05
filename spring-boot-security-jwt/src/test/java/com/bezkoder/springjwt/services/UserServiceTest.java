package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.parser.Entity;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.bezkoder.springjwt.models.ERole.ROLE_USER;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Test
    void createUserSetsTheNameandSave(){
        User user = new User();
        user.setUsername("test");
        user.setId(123L);
        Set<Role> roles = new HashSet<Role>();
        roles.add(new Role(ROLE_USER));
        user.setRoles(roles);
        user.setEmail("955@gmail.com");
        userRepository.save(user);
        when(userRepository
                .findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));
        assertNotNull(userRepository.findByUsername("test").get().getUsername());
    }
}
