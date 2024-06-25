package com.example.demo.post.domain.dto;

import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.Users;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostResponseTest {
    @Test
    void Post으로_응답을_생성할_수_있다(){
        //given
        Post post = Post.builder()
                .content("helloworld")
                .writer(Users.builder()
                        .email("sungmin4218@gmail.com")
                        .nickname("code200jade")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                        .build())
                .build();

        //when
        PostResponse postResponse = PostResponse.from(post);

        //then
        assertAll(()->{
            assertThat(postResponse.getContent()).isEqualTo("helloworld");
            assertThat(postResponse.getWriter().getEmail()).isEqualTo("sungmin4218@gmail.com");
            assertThat(postResponse.getWriter().getNickname()).isEqualTo("code200jade");
            assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        });
    }
}