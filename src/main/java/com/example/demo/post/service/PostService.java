package com.example.demo.post.service;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.dto.PostCreate;
import com.example.demo.post.domain.dto.PostUpdate;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.Users;

import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public Post getById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public Post create(PostCreate postCreate) {
        Users writer = userService.getById(postCreate.getWriterId());
        Post post = Post.from(writer, postCreate);
        return postRepository.save(post);
    }

    public Post update(long id, PostUpdate postUpdate) {
        Post post = getById(id);
        post = post.update(postUpdate);
        return postRepository.save(post);
    }
}