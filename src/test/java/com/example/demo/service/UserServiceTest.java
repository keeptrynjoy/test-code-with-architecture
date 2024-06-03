package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.*;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {
    @Autowired
    private UserService userService;

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
}