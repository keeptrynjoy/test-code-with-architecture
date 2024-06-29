package com.example.demo.user.domain;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Users {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String address;
    private final String certificationCode;
    private final UserStatus status;
    private final Long lastLoginAt;

    @Builder
    public Users(Long id, String email, String nickname, String address, String certificationCode, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.certificationCode = certificationCode;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static Users from(UserCreate userCreate, UuidHolder uuidHolder) {
        return Users.builder()
                .email(userCreate.getEmail())
                .nickname(userCreate.getNickname())
                .address(userCreate.getAddress())
                .status(UserStatus.PENDING)
                .certificationCode(uuidHolder.random())
                .build();
    }

    public Users update(UserUpdate userUpdate) {
        return Users.builder()
                .id(id)
                .email(email)
                .nickname(userUpdate.getNickname())
                .address(userUpdate.getAddress())
                .certificationCode(certificationCode)
                .status(status)
                .lastLoginAt(lastLoginAt)
                .build();
    }

    public Users login(ClockHolder clockHolder){
        return Users.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .certificationCode(certificationCode)
                .status(status)
                .lastLoginAt(clockHolder.millis())
                .build();
    }

    public Users certificate(String certificationCode) {
        if(!this.certificationCode.equals(certificationCode)){
            throw new CertificationCodeNotMatchedException();
        }
        return Users.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .certificationCode(certificationCode)
                .status(UserStatus.ACTIVE)
                .lastLoginAt(lastLoginAt)
                .build();
    }
}
