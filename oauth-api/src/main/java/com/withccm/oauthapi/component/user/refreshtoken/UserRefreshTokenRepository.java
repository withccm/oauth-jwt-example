package com.withccm.oauthapi.component.user.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    // UserRefreshToken findByUserNo(Long userNo);
    UserRefreshToken findByUserNoAndAccessToken(Long userNo, String accessToken);
    UserRefreshToken findByUserNoAndRefreshToken(Long userNo, String refreshToken);
}
