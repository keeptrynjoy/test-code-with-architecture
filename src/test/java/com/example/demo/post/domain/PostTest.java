package com.example.demo.post.domain;

import com.example.demo.post.domain.dto.PostCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.Users;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class PostTest {

    @Test
    public void PostCreate으로_게시물을_만들_수_있다(){
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloworld")
                .build();

        Users writer = Users.builder()
                .email("sungmin4218@gmail.com")
                .nickname("code200jade")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                .build();

        //when
        Post post = Post.from(writer, postCreate);

        //then
        assertAll(()->{
            assertThat(post.getContent()).isEqualTo("helloworld");
            assertThat(post.getWriter().getEmail()).isEqualTo("sungmin4218@gmail.com");
            assertThat(post.getWriter().getNickname()).isEqualTo("code200jade");
            assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
            assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
            assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa");
        });
    }

}