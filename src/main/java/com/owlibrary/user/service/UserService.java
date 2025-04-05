package com.owlibrary.user.service;

import com.owlibrary.common.exception.CustomException;
import com.owlibrary.common.exception.ErrorCode;
import com.owlibrary.user.domain.User;
import com.owlibrary.user.dto.SignupRequest;
import com.owlibrary.user.dto.SignupResponse;
import com.owlibrary.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
