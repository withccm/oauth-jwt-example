package com.withccm.oauthapi.component.user;

import com.withccm.oauthapi.component.user.enums.OAuthAttributes;
import com.withccm.oauthapi.component.user.enums.Role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 소셜 로그인에서 데이터를 가져와서 만드는 객체
 */
@RequiredArgsConstructor
@Getter
public class UserProfile {
    private final OAuthAttributes oauthType;
    private final String oauthId;
    private final String name;
    private final String email;
    private final String imageUrl;

    public User toUser() {  // 여기 함수는 최초로 가입할때 호출
        return new User(oauthType, oauthId, name, email, imageUrl, Role.USER);
    }
}
