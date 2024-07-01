package com.example.demo.user.controller.port;

import com.example.demo.user.domain.Users;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;

public interface UserService {
    Users getByEmail(String email);
    Users getById(long id);
    Users create(UserCreate userCreate);
    Users update(long id, UserUpdate userUpdate);
    void login(long id);
    void verifyEmail(long id, String certificationCode);
}
