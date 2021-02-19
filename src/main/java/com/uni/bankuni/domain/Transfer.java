package com.uni.bankuni.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "transfers")
public class Transfer {

    @Id
    private String id;

    private Date startDate;
    private Date endDate;
    private String message;
    private String type;
    private double amount;
    private String sender;
    private String receiver;
}
