package com.example.demo.user.controller.response;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.Users;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MyProfileResponseTest {

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
        MyProfileResponse myProfileResponse = MyProfileResponse.from(users);

        //then
        assertAll(()->{
            assertThat(myProfileResponse.getId()).isEqualTo(1);
            assertThat(myProfileResponse.getEmail()).isEqualTo("sungmin4218@gmail.com");
            assertThat(myProfileResponse.getNickname()).isEqualTo("code200jade");
            assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
            assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
            assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
        });
    }

}