package com.uni.bankuni.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class TransferRequest {

    @Id
    private String id;

    private String sender;
    private String receiver;
    private String message;
    private double amount;
}
