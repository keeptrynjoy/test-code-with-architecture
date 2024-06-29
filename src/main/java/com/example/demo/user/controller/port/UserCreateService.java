package com.example.demo.user.controller.port;

import com.example.demo.user.domain.Users;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;

public interface UserCreateService {
    Users create(UserCreate userCreate);
}
