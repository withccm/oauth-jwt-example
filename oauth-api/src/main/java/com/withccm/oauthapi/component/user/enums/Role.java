package com.withccm.oauthapi.component.user.enums;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Role {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public static Role of(String code) {
        return Arrays.stream(Role.values())
            .filter(r -> r.getKey().equals(code))
            .findAny()
            .orElse(GUEST);
    }
}
