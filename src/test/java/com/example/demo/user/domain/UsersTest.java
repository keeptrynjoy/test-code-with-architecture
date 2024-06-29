package com.example.demo.user.domain;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsersTest {

    @Test
    public void Users는_UserCreate_객체로_생성할_수_있다(){
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("ijkim246@naver.com")
                .nickname("code200jade")
                .address("New York")
                .build();

        //when
        Users users = Users.from(userCreate, new TestUuidHolder("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa"));

        //then
        assertAll(()->{
            assertThat(users.getId()).isNull();
            assertThat(users.getEmail()).isEqualTo("ijkim246@naver.com");
            assertThat(users.getNickname()).isEqualTo("code200jade");
            assertThat(users.getAddress()).isEqualTo("New York");
            assertThat(users.getStatus()).isEqualTo(UserStatus.PENDING);
            assertThat(users.getCertificationCode()).isEqualTo("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa");
        });
    }

    @Test
    public void Users는_UserUpdate_객체로_데이터를_업데이트_할_수_있다(){
        //given
        Users user = Users.builder()
                .id(1L)
                .email("ijkim246@naver.com")
                .nickname("code200jade")
                .address("New York")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("code500jade")
                .address("Seoul")
                .build();

        //when
        user = user.update(userUpdate);

        //then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("ijkim246@naver.com");
        assertThat(user.getNickname()).isEqualTo("code500jade");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa");
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    public void Users는_로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다(){
        //given
        Users user = Users.builder()
                .id(1L)
                .email("ijkim246@naver.com")
                .nickname("code200jade")
                .address("New York")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                .build();

        //when
        user = user.login(new TestClockHolder(1678530673958L));

        //then
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    public void 유효한_인증_코드로_계정을_활성화_할_수_있다(){
        //given
        Users user = Users.builder()
                .id(1L)
                .email("ijkim246@naver.com")
                .nickname("code200jade")
                .address("New York")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                .build();

        //when
        user = user.certificate("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa");

        //then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void Users는_잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다(){
        //given
        Users user = Users.builder()
                .id(1L)
                .email("ijkim246@naver.com")
                .nickname("code200jade")
                .address("New York")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                .build();

        // when & then
        assertThatThrownBy(()->
            user.certificate("aaaaaaa-aaaaaa-aaaaa-aaaaaaaab")
        ).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}