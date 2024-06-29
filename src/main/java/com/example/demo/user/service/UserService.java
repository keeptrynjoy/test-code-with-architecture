package com.example.demo.user.service;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.Users;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;

import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Builder
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CertificationService certificationService;
    private final UuidHolder uuidHolder;
    private final ClockHolder clockHolder;

    public Users getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", email));
    }

    public Users getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    @Transactional
    public Users create(UserCreate userCreate) {
        Users users = Users.from(userCreate, uuidHolder);
        users = userRepository.save(users);
        certificationService.send(userCreate.getEmail(), users.getId(), users.getCertificationCode());
        return users;
    }

    @Transactional
    public Users update(long id, UserUpdate userUpdate) {
        Users user = getById(id);
        user = user.update(userUpdate);
        user = userRepository.save(user);
        return user;
    }


    @Transactional
    public void login(long id) {
        Users user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user = user.login(clockHolder);
        userRepository.save(user);
    }

    @Transactional
    public void verifyEmail(long id, String certificationCode) {
        Users users = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        users = users.certificate(certificationCode);
        userRepository.save(users);
    }
}