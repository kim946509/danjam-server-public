package com.example.danjamserver.springSecurity.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    STRANGER("ROLE_STRANGER"),
    FULL_USER("ROLE_FULL_USER"),
    AUTH_USER("ROLE_AUTH_USER"),
    ADMIN("ROLE_ADMIN");

    private final String roleValue;

    public static Role fromString(String roleStr) {
        for (Role role : Role.values()) {
            if (role.getRoleValue().equalsIgnoreCase(roleStr)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role with text: " + roleStr);
    }
}