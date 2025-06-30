package io.github.akumosstl.auth.controller;

import io.github.akumosstl.auth.model.ERole;
import io.github.akumosstl.auth.model.Role;
import io.github.akumosstl.auth.model.User;
import io.github.akumosstl.auth.payload.request.SignupRequest;
import io.github.akumosstl.auth.payload.response.MessageResponse;
import io.github.akumosstl.auth.repository.RoleRepository;
import io.github.akumosstl.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final String ROLE_IS_NOT_FOUND = "[Error]: Role is not found.";

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("[Error]: Username is already taken!", 500));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("[Error]: Email is already in use!", 500));
        }
        var user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> rolesFromRequest = signUpRequest.getRole();
        var roles = new HashSet<Role>();

        if (rolesFromRequest == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));
            roles.add(userRole);
        } else {
            rolesFromRequest.forEach(role -> {
                if (role.equals("admin")) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("[INFO]: User registered successfully!", 201));
    }

}
