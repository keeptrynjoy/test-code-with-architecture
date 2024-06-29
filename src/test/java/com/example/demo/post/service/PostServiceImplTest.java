package com.example.demo.post.service;

import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.dto.PostCreate;
import com.example.demo.post.domain.dto.PostUpdate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PostServiceImplTest {
    private PostServiceImpl postServiceImpl;

    @BeforeEach
    void init() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.postServiceImpl = PostServiceImpl.builder()
                .postRepository(fakePostRepository)
                .userRepository(fakeUserRepository)
                .clockHolder(new TestClockHolder(1679530673958L))
                .build();
        Users user1 = Users.builder()
                .id(1L)
                .email("sungmin4218@gmail.com")
                .nickname("code200jade")
                .address("Seoul")
                .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build();
        Users user2 = Users.builder()
                .id(2L)
                .email("sungmin1234@gmail.com")
                .nickname("code300jade")
                .address("Seoul")
                .certificationCode("aaaaaaa-aaaaaa-aaaaa-aaaaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build();
        fakeUserRepository.save(user1);
        fakeUserRepository.save(user2);
        fakePostRepository.save(Post.builder()
                .id(1L)
                .content("helloworld")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .writer(user1)
                .build());
    }

    @Test
    void getById는_존재하는_게시물을_내려준다() {
        // given
        // when
        Post result = postServiceImpl.getById(1);

        // then
        assertThat(result.getContent()).isEqualTo("helloworld");
        assertThat(result.getWriter().getEmail()).isEqualTo("sungmin4218@gmail.com");
    }

    @Test
    void postCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("foobar")
                .build();

        // when
        Post result = postServiceImpl.create(postCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar");
        assertThat(result.getCreatedAt()).isEqualTo(1679530673958L);
    }

    @Test
    void postUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world :)")
                .build();

        // when
        postServiceImpl.update(1, postUpdate);

        // then
        Post post = postServiceImpl.getById(1);
        assertThat(post.getContent()).isEqualTo("hello world :)");
        assertThat(post.getModifiedAt()).isEqualTo(1679530673958L);
    }


}