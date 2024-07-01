package com.example.demo.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.dto.PostCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;


public class PostCreateControllerTest {

    @Test
    void 사용자는_게시물을_작성할_수_있다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(()-> 1679530673958L)
                .build();
        testContainer.userRepository.save(Users.builder()
                .id(1L)
                .email("sungmin4218@gmail.com")
                .nickname("code200jade")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .build()
        );

        PostCreate postCreate = PostCreate.builder()
                .content("helloworld")
                .writerId(1)
                .build();

        // when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.create(postCreate);

        // then
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("helloworld");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("code200jade");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(1679530673958L);
    }
}
