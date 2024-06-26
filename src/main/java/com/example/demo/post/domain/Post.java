package com.example.demo.post.domain;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.post.domain.dto.PostCreate;
import com.example.demo.post.domain.dto.PostUpdate;
import com.example.demo.user.domain.Users;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;

@Getter
public class Post {
    private final Long id;
    private final String content;
    private final Long createdAt;
    private final Long modifiedAt;
    private final Users writer;

    @Builder
    public Post(Long id, String content, Long createdAt, Long modifiedAt, Users writer) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
    }

    public static Post from(Users writer, PostCreate postCreate, ClockHolder clockHolder) {
        return Post.builder()
                .content(postCreate.getContent())
                .writer(writer)
                .createdAt(clockHolder.millis())
                .build();
    }


    public Post update(PostUpdate postUpdate, ClockHolder clockHolder) {
        return Post.builder()
                .id(id)
                .content(postUpdate.getContent())
                .createdAt(createdAt)
                .modifiedAt(clockHolder.millis())
                .writer(writer)
                .build();
    }
}
