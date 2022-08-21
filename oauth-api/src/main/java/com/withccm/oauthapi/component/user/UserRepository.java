package com.withccm.oauthapi.component.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withccm.oauthapi.component.user.enums.OAuthAttributes;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthTypeAndOauthId(OAuthAttributes oauthType, String id);
}
