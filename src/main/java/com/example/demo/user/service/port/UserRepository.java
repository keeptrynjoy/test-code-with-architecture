package com.example.demo.user.service.port;

import com.example.demo.user.domain.Users;
import com.example.demo.user.domain.UserStatus;

import java.util.Optional;

public interface UserRepository {
    Users getById(long writerId);

    Optional<Users> findById(long id);

    Optional<Users> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<Users> findByEmailAndStatus(String email, UserStatus userStatus);

    Users save(Users user);
}
