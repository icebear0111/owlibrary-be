package com.owlibrary.user.repository;

import com.owlibrary.user.domain.SocialProvider;
import com.owlibrary.user.domain.UserSocialLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSocialLinkRepository extends JpaRepository<UserSocialLink, Long> {
    Optional<UserSocialLink> findByProviderAndProviderUserId(SocialProvider provider, String providerUserId);
}
