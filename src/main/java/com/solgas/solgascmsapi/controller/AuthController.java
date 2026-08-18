package com.solgas.solgascmsapi.controller;

import com.solgas.solgascmsapi.dto.LoginRequest;
import com.solgas.solgascmsapi.dto.LoginResponse;
import com.solgas.solgascmsapi.service.JwtTokenService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping({"/signin", "/login"})
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring("ROLE_".length()))
                .findFirst()
                .orElse("USER");
        String token = jwtTokenService.createToken(authentication.getName(), role);
        return new LoginResponse(token, "Bearer", jwtTokenService.expirationSeconds());
    }

    @GetMapping("/me")
    public Map<String, String> me(Authentication authentication) {
        String username = authentication.getName();
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            username = jwt.getSubject();
        }
        return Map.of("username", username);
    }
}
