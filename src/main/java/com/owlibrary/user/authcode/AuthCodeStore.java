package com.owlibrary.user.authcode;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthCodeStore {

    private record Entry(String code, LocalDateTime expiresAt) {}

    private final Map<String, Entry> store = new ConcurrentHashMap<>();

    @Getter
    private String lastVerifiedUsername;

    public void save(String username, String code, int ttlSeconds) {
        store.put(username, new Entry(code, LocalDateTime.now().plusSeconds(ttlSeconds)));
    }

    public boolean verify(String username, String code) {
        Entry entry = store.get(username);
        if (entry == null) return false;
        if (entry.expiresAt().isBefore(LocalDateTime.now())) return false;
        return entry.code().equals(code);
    }

    public void markVerified(String username) {
        this.lastVerifiedUsername = username;
    }

    public void remove(String username) {
        store.remove(username);
        if (username.equals(lastVerifiedUsername)) {
            lastVerifiedUsername = null;
        }
    }
}