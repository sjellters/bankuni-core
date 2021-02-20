package com.uni.bankuni.domain;

import lombok.Data;

@Data
public class TransferRequest {
    private String sender;
    private String receiver;
    private String message;
    private double amount;
}
