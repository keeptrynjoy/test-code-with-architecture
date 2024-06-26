package com.example.demo.user.controller.response;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.Users;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void Users으로_응답을_생성할_수_있다(){
        //given
        Users users = Users.builder()
                .id(1L)
                .email("sungmin4218@gmail.com")
                .nickname("code200jade")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                .build();

        //when
        UserResponse userResponse = UserResponse.from(users);

        //then
        assertAll(()->{
            assertThat(userResponse.getId()).isEqualTo(1);
            assertThat(userResponse.getEmail()).isEqualTo("sungmin4218@gmail.com");
            assertThat(userResponse.getNickname()).isEqualTo("code200jade");
            assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
            assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
        });
    }

}