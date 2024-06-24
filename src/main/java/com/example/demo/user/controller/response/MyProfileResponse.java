package com.example.demo.user.controller.response;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class MyProfileResponse {

    private Long id;
    private String email;
    private String nickname;
    private String address;
    private UserStatus status;
    private Long lastLoginAt;

    public static MyProfileResponse from(Users users) {
        return MyProfileResponse.builder()
                .id(users.getId())
                .email(users.getEmail())
                .nickname(users.getNickname())
                .address(users.getAddress())
                .status(users.getStatus())
                .lastLoginAt(users.getLastLoginAt())
                .build();
    }
}
