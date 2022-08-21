package com.withccm.oauthapi.component.user.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;

import com.withccm.oauthapi.component.user.UserProfile;

import lombok.Getter;

public enum OAuthAttributes {
    GITHUB("github", (attributes, oauthType) -> {
        return new UserProfile(
                oauthType,
                String.valueOf(attributes.get("id")),
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("avatar_url")
        );
    }),
    GOOGLE("google", (attributes, oauthType) -> {
        return new UserProfile(
                oauthType,
                String.valueOf(attributes.get("sub")),
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("picture")
        );
    }),
    NAVER("naver", (attributes, oauthType) -> {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return new UserProfile(
                oauthType,
                (String) response.get("id"),
                (String) response.get("name"),
                (String) response.get("email"),
                (String) response.get("profile_image")
        );
    });

    @Getter
    private final String registrationId;
    private final BiFunction<Map<String, Object>, OAuthAttributes, UserProfile> of;

    OAuthAttributes(String registrationId, BiFunction<Map<String, Object>, OAuthAttributes, UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        OAuthAttributes oAuthAttributes = Arrays.stream(values())
            .filter(provider -> registrationId.equals(provider.registrationId))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
        return oAuthAttributes
                .of.apply(attributes, oAuthAttributes);
    }
}
