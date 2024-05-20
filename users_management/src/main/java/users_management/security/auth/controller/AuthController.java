package users_management.security.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import users_management.entities.User;
import users_management.repositories.UserRepository;
import users_management.security.auth.service.UserDetailsImpl;
import users_management.security.jwt.JwtUtils;
import users_management.security.payload.JwtResponse;
import users_management.security.payload.UserLoginRequest;
import users_management.services.UserService;

import javax.validation.Valid;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()).get(0);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                role));
    }



    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("Received Authorization header: " + authorizationHeader);

        String token = authorizationHeader.substring("Bearer ".length());
        System.out.println("Extracted Token: " + token);

        SecurityContextHolder.clearContext();

        jwtUtils.revokeToken(token);

        System.out.println(jwtUtils.revokedTokens);
        return ResponseEntity.ok("{\"message\": \"Logged out successfully\"}");
    }

    @GetMapping("/username")
    public ResponseEntity<String> getUsernameFromToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        String username = jwtUtils.getUserNameFromJwtToken(token);
         return ResponseEntity.ok(username);
    }

}