package com.owlibrary.user.service;

import com.owlibrary.common.exception.CustomException;
import com.owlibrary.common.exception.ErrorCode;
import com.owlibrary.user.authcode.AuthCodeService;
import com.owlibrary.user.domain.User;
import com.owlibrary.user.dto.*;
import com.owlibrary.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthCodeService authCodeService;

    @Transactional
    public SignupResponse signUp(SignupRequest request) {
        // 중복 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.USERNAME_DUPLICATED);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new CustomException(ErrorCode.PHONE_DUPLICATED);
        }

        // 비밀번호 검증
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 사용자 생성
        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .profileImageUrl(null) // 기본 프로필 이미지 URL 설정
                .build();

        userRepository.save(user);

        return new SignupResponse("회원가입이 완료되었습니다.");
    }

    public FindUsernameResponse findUsername(FindUsernameRequest request) {
        Optional<User> result = Optional.empty();

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            result = userRepository.findByNameAndEmail(request.getName(), request.getEmail());
        } else if (request.getPhone() != null && !request.getPhone().isBlank()) {
            result = userRepository.findByNameAndPhone(request.getName(), request.getPhone());
        }

        User user = result.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String username = user.getUsername();
        String masked = username.substring(0, 4) + "*".repeat(username.length() - 4);

        return new FindUsernameResponse(masked);
    }

    public void sendResetPasswordCode(PasswordResetRequest request) {
        User user = findUserByUsernameAndEmailOrPhone(request);
        authCodeService.generateAndSend(user.getUsername(), request.getEmail() != null ? request.getEmail() : request.getPhone());
    }

    public void verifyResetPasswordCode(PasswordResetCodeVerifyRequest request) {
        if (!authCodeService.verifyCode(request.getUsername(), request.getCode())) {
            throw new CustomException(ErrorCode.INVALID_AUTH_CODE);
        }
        authCodeService.markVerified(request.getUsername());
    }

    @Transactional
    public PasswordResetResponse resetPassword(PasswordResetConfirmRequest request) {
        String username = authCodeService.getLastVerifiedUsername();
        if (username == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_RESET);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!request.getNewPassword().equals(request.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        authCodeService.removeCode(username);

        return new PasswordResetResponse("비밀번호가 변경되었습니다.");
    }

    private User findUserByUsernameAndEmailOrPhone(PasswordResetRequest request) {
        Optional<User> result = Optional.empty();

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            result = userRepository.findByUsernameAndEmail(request.getUsername(), request.getEmail());
        } else if (request.getPhone() != null && !request.getPhone().isBlank()) {
            result = userRepository.findByUsernameAndPhone(request.getUsername(), request.getPhone());
        }

        return result.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateProfileImage(String username, UpdateProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateProfileImageUrl(request.getProfileImageUrl());
    }
}
