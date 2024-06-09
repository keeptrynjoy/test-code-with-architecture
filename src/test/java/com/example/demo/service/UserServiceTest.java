package com.example.demo.service;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.context.jdbc.Sql.*;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다(){
        //given
        String email = "sungmin4218@gmail.com";

        //when
        UserEntity byEmail = userService.getByEmail(email);

        //then
        assertThat(byEmail.getNickname()).isEqualTo("code200jade");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다(){
        //given
        String email = "sungmin1234@gmail.com";

        //when & then
        assertThatThrownBy(() -> {
            userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getByI는_ACTIVE_상태인_유저를_찾아올_수_있다(){
        //given & when
        UserEntity byEmail = userService.getById(1);

        //then
        assertThat(byEmail.getNickname()).isEqualTo("code200jade");
    }

    @Test
    void getById는_PENDING_상태인_유저를_찾아올_수_없다(){
        //given & when & then
        assertThatThrownBy(() -> {
            userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto를_이용하여_유저를_생성할_수_있다(){
        //given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("sungmin200@gmail.com")
                .address("Busan")
                .nickname("sungmin")
                .build();

        // 이메일을 발송하는 JavaMailSender를 대체하기 위해 Mock사용
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //when
        UserEntity result = userService.create(userCreateDto);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    void userUpdateDto를_이용하여_유저를_수정할_수_있다(){
        //given
        UserUpdateDto userUpdate = UserUpdateDto.builder()
                .address("Masan")
                .nickname("change")
                .build();

        //when
        userService.update(1,userUpdate);

        //then
        UserEntity userEntity = userService.getById(1);

        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("Masan");
        assertThat(userEntity.getNickname()).isEqualTo("change");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간에_변경된다(){
        //given & when
        userService.login(1);

        //then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
        // assertThat(userEntity.getLastLoginAt()).isEqualTo("??"); // FIXME
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다(){
        //given & when
        userService.verifyEmail(2,"aaaaaaa-aaaaaa-aaaaa-aaaaaaaab");

        //then
        UserEntity userEntity = userService.getById(2);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다(){
        assertThatThrownBy(()->{
            userService.verifyEmail(2,"aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa");
                }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}