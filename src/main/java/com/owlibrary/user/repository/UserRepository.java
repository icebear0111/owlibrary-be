package com.owlibrary.user.repository;

import com.owlibrary.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByNameAndEmail(String name, String email);
    Optional<User> findByNameAndPhone(String name, String phone);
    Optional<User> findByUsernameAndEmail(String username, String email);
    Optional<User> findByUsernameAndPhone(String username, String phone);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

}
