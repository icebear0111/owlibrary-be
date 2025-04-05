package com.owlibrary.user.authcode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthCodeService {

    private final AuthCodeStore authCodeStore;

    public void generateAndSend(String username, String emailOrPhone) {
        String code = String.format("%06d", new Random().nextInt(999999));
        authCodeStore.save(username, code, 300); // 유효시간 5분

        // TODO: 실제 전송 로직
        System.out.println("📨 인증코드 전송 대상: " + emailOrPhone);
        System.out.println("✅ 인증코드: " + code);
    }

    public boolean verifyCode(String username, String inputCode) {
        return authCodeStore.verify(username, inputCode);
    }

    public void markVerified(String username) {
        authCodeStore.markVerified(username);
    }

    public String getLastVerifiedUsername() {
        return authCodeStore.getLastVerifiedUsername();
    }

    public void removeCode(String username) {
        authCodeStore.remove(username);
    }
}