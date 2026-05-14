package com.parking.service;

import com.parking.dto.request.LoginRequest;
import com.parking.dto.response.AuthResponse;
import com.parking.dto.response.UserResponse;
import com.parking.exception.ResourceNotFoundException;
import com.parking.repository.UserRepository;
import com.parking.security.JwtTokenProvider;
import com.parking.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AuthResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        String accessToken = tokenProvider.generateAccessToken(principal.getEmail());
        String refreshToken = tokenProvider.generateRefreshToken(principal.getEmail());

        UserResponse userResponse = userRepository.findByEmail(principal.getEmail())
                .map(UserResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("User", principal.getEmail()));

        return AuthResponse.of(accessToken, refreshToken, userResponse);
    }

    public AuthResponse refresh(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalStateException("Invalid or expired refresh token");
        }
        String email = tokenProvider.getEmailFromToken(refreshToken);
        String newAccessToken = tokenProvider.generateAccessToken(email);
        String newRefreshToken = tokenProvider.generateRefreshToken(email);

        UserResponse userResponse = userRepository.findByEmail(email)
                .map(UserResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));

        return AuthResponse.of(newAccessToken, newRefreshToken, userResponse);
    }
}
