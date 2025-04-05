package com.owlibrary.auth.service;

import com.owlibrary.auth.dto.LoginRequest;
import com.owlibrary.auth.dto.LoginResponse;
import com.owlibrary.auth.dto.TokenRefreshResponse;
import com.owlibrary.auth.jwt.JwtTokenProvider;
import com.owlibrary.common.exception.CustomException;
import com.owlibrary.common.exception.ErrorCode;
import com.owlibrary.user.domain.User;
import com.owlibrary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_LOGIN));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_LOGIN);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        return new LoginResponse(accessToken, refreshToken);
    }

    public TokenRefreshResponse refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(username);

        return new TokenRefreshResponse(newAccessToken);
    }
}
