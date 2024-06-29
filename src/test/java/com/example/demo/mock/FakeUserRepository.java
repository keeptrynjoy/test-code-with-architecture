package com.example.demo.mock;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.Users;
import com.example.demo.user.service.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<Users> data = new ArrayList<>();

    @Override
    public Users getById(long writerId) {
        return data.stream()
                .filter(user -> Objects.equals(user.getId(), writerId))
                .findAny()
                .orElseThrow(()->
                        new ResourceNotFoundException("User", writerId)
                );
    }

    @Override
    public Optional<Users> findById(long id) {
        return data.stream().filter(user -> Objects.equals(user.getId(), id)).findAny();
    }

    @Override
    public Optional<Users> findByIdAndStatus(long id, UserStatus userStatus) {
        return data.stream().filter(user -> Objects.equals(user.getId(), id) && user.getStatus() == userStatus).findAny();
    }

    @Override
    public Optional<Users> findByEmailAndStatus(String email, UserStatus userStatus) {
        return data.stream().filter(user -> Objects.equals(user.getEmail(), email) && user.getStatus() == userStatus).findAny();
    }

    @Override
    public Users save(Users user) {
        if(user.getId() == null || user.getId() == 0){
            Users newUser = Users.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .address(user.getAddress())
                    .certificationCode(user.getCertificationCode())
                    .status(user.getStatus())
                    .lastLoginAt(user.getLastLoginAt())
                    .build();
            data.add(newUser);
            return newUser;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), user.getId()));
            data.add(user);
            return user;
        }
    }


}