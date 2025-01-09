package com.example.danjamserver.admin.dto;

import lombok.Getter;

@Getter
public class UserAuthenticationRequest {
    private String username;
    private Boolean isApproved;
    private String description;
}