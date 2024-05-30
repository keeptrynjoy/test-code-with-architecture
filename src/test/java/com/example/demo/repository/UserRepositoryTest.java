package com.example.demo.repository;

import com.example.demo.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/sql/user-repository-test-data.sql")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;


    @Test
    void findByIdAndStatus로_유저_데이터를_찾을_수_있다(){
        //given
        //when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1,UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus는_데이터가_없으면_Optional_empty를_내려준다(){
        //given
        //when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1,UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus로_유저_데이터를_찾을_수_있다(){
        //given
        //when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("sungmin4218@gmail.com",UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus는_데이터가_없으면_Optional_empty를_내려준다(){
        //given
        //when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("sungmin4218@gmail.com",UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();
    }
}