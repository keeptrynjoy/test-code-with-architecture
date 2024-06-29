package com.example.demo.user.service;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.Users;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserServiceImplTest {
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void init(){
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.userServiceImpl = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa"))
                .clockHolder(new TestClockHolder(1678530673958L))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .build();

        fakeUserRepository.save(Users.builder()
                .id(1L)
                .email("sungmin4218@gmail.com")
                .nickname("code200jade")
                .address("Seoul")
                .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build()
        );

        fakeUserRepository.save(Users.builder()
                .id(2L)
                .email("sungmin1234@gmail.com")
                .nickname("code300jade")
                .address("Seoul")
                .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build()
        );
    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다(){
        //given
        String email = "sungmin4218@gmail.com";

        //when
        Users users = userServiceImpl.getByEmail(email);

        //then
        assertThat(users.getNickname()).isEqualTo("code200jade");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다(){
        //given
        String email = "sungmin1234@gmail.com";

        //when & then
        assertThatThrownBy(() -> {
            userServiceImpl.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_ACTIVE_상태인_유저를_찾아올_수_있다(){
        //given & when
        Users byEmail = userServiceImpl.getById(1);

        //then
        assertThat(byEmail.getNickname()).isEqualTo("code200jade");
    }

    @Test
    void getById는_PENDING_상태인_유저를_찾아올_수_없다(){
        //given & when & then
        assertThatThrownBy(() -> {
            userServiceImpl.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto를_이용하여_유저를_생성할_수_있다(){
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("sungmin200@gmail.com")
                .address("Busan")
                .nickname("sungmin")
                .build();


        //when
        Users result = userServiceImpl.create(userCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa");
    }

    @Test
    void userUpdateDto를_이용하여_유저를_수정할_수_있다(){
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Masan")
                .nickname("change")
                .build();

        //when
        userServiceImpl.update(1,userUpdate);

        //then
        Users userEntity = userServiceImpl.getById(1);

        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("Masan");
        assertThat(userEntity.getNickname()).isEqualTo("change");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간에_변경된다(){
        //given & when
        userServiceImpl.login(1);

        //then
        Users userEntity = userServiceImpl.getById(1);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
        assertThat(userEntity.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다(){
        //given & when
        userServiceImpl.verifyEmail(2,"aaaaaaa-aaaaaa-aaaaa-aaaaaaaab");

        //then
        Users userEntity = userServiceImpl.getById(2L);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다(){
        assertThatThrownBy(()->{
            userServiceImpl.verifyEmail(2,"aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa");
                }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}