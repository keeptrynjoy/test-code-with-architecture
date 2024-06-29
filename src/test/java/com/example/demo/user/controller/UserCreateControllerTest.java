package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserCreate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreateControllerTest {

    @Test
    void 사용자는_회원_가입을_할_수_있고_회원가입된_사용자는_PENDING_상태이다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .uuidHolder(()-> "aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                .build();
        UserCreate userCreate = UserCreate.builder()
                .email("sungmin4218@gmail.com")
                .nickname("code200jade")
                .address("Seoul")
                .build();

        // when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(userCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("sungmin4218@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("code200jade");
        assertThat(result.getBody().getLastLoginAt()).isNull();
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(testContainer.userRepository.getById(1).getCertificationCode())
                .isEqualTo("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa");
    }
}