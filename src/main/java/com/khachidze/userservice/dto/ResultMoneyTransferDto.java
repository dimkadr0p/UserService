package com.khachidze.userservice.dto;

import com.khachidze.userservice.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultMoneyTransferDto {
    private UserEntity sender;
    private UserEntity receiver;
}
