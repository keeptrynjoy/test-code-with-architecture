package com.example.demo.post.controller;

import static org.assertj.core.api.Assertions.*;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.dto.PostUpdate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.Users;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class PostControllerTest {

    @Test
    void 사용자는_게시물을_단건_조회_할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.postRepository.save(Post.builder()
                .content("helloworld")
                .writer(Users.builder()
                        .email("sungmin4218@gmail.com")
                        .nickname("code200jade")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                        .build())
                .build()
        );

        // when
        ResponseEntity<PostResponse> result = testContainer.postController.getPostById(1);

        // then
        assertThat(Objects.requireNonNull(result.getBody()).getContent()).isEqualTo("helloworld");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("sungmin4218@gmail.com");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("code200jade");
        assertThat(result.getBody().getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .build();

        // when
        // then
        assertThatThrownBy(()->
                testContainer.postController.getPostById(1)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_게시물을_수정할_수_있다(){
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(()-> 1679530673958L)
                .build();
        testContainer.postRepository.save(Post.builder()
                .content("helloworld")
                .writer(Users.builder()
                        .email("sungmin4218@gmail.com")
                        .nickname("code200jade")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                        .build())
                .build()
        );

        PostUpdate postUpdate = PostUpdate.builder()
                .content("foobar")
                .build();

        // when
        ResponseEntity<PostResponse> result = testContainer.postController.updatePost(1, postUpdate);

        // then
        assertThat(Objects.requireNonNull(result.getBody()).getContent()).isEqualTo("foobar");
        assertThat(testContainer.postRepository.findById(1).get().getModifiedAt()).isEqualTo(1679530673958L);
    }
}
