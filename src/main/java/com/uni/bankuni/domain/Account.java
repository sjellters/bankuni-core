package com.uni.bankuni.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "accounts")
public class Account {

    @Id
    private String id;

    private String owner;
    private double amount;
    private boolean transferAvailable;
}
