package com.khachidze.userservice.service;

import com.khachidze.userservice.dto.ResultMoneyTransferDto;
import com.khachidze.userservice.entity.UserEntity;
import com.khachidze.userservice.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class UserService {
    @Inject
    private UserRepository userRepository;

    public UserEntity createUser(UserEntity userRequest) {
        UserEntity user = UserEntity.builder()
                .createdAt(new Date())
                .balance(userRequest.getBalance())
                .phoneNumber(userRequest.getPhoneNumber())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .build();

        userRepository.save(user);

        return user;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public void updateBalanceUsers(ResultMoneyTransferDto resultMoneyTransferDto) {
        UserEntity sender = resultMoneyTransferDto.getSender();
        UserEntity receiver = resultMoneyTransferDto.getReceiver();

        UserEntity senderEntity = getUserByPhoneNumber(sender.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        UserEntity receiverEntity = getUserByPhoneNumber(receiver.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        senderEntity.setBalance(sender.getBalance());
        senderEntity.setUpdatedAt(new Date());

        receiverEntity.setBalance(receiver.getBalance());
        receiverEntity.setUpdatedAt(new Date());

        userRepository.updateBalance(senderEntity, receiverEntity);
    }
}
