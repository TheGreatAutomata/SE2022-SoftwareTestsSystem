package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.DeleteRequest;
import com.bezkoder.springjwt.payload.request.ModifyPasswordRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("")
public class AdminController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;


    /**
     * Register mod response entity.
     *
     * @param signUpRequest the sign up request
     * @return the response entity
     */
    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerMod(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "mod":
                        Role modRole = roleRepository
                                .findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    case "mod_test":
                        Role modTestRole = roleRepository
                                .findByName(ERole.ROLE_MODTEST)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modTestRole);
                        break;
                    case "mod_market":
                        Role modMarketRole = roleRepository
                                .findByName(ERole.ROLE_MODMARKET)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modMarketRole);
                        break;
                    case "mod_qlty":
                        Role modQLTYROLE = roleRepository
                                .findByName(ERole.ROLE_MODQLTY)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modQLTYROLE);
                        break;
                    default:
                        Role userRole = roleRepository
                                .findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    /**
     * Delete user response entity.
     *
     * @param deleteRequest the delete request
     * @return the response entity
     */
    @PostMapping("delete/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@Valid @RequestBody DeleteRequest deleteRequest){
        if (!userRepository.existsById(deleteRequest.getId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (!deleteRequest.getId().equals(deleteRequest.getConfirmed())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The two inputs are different!"));
        }
        userRepository.deleteById(deleteRequest.getId());
        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }

    /**
     * Modify pass word response entity.
     *
     * @param modifyPasswordRequest the modify password request
     * @return the response entity
     */
    @PostMapping("modify/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modifyPassWord(@Valid @RequestBody ModifyPasswordRequest modifyPasswordRequest){
        if (!modifyPasswordRequest.getNewpassword().equals(modifyPasswordRequest.getConfirmpassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The two inputs are different!"));
        }
        Optional<User> userOptional = userRepository.findByUsername(modifyPasswordRequest.getUsername());
        if(userOptional.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User doesnt exist"));
        }
        User user = userOptional.get();
        user.setPassword(encoder.encode(modifyPasswordRequest.getConfirmpassword()));
        userRepository.save(user);
        return ResponseEntity.ok(
                new MessageResponse("User deleted successfully!")
        );
    }
}