package com.uni.bankuni.repository;

import com.uni.bankuni.domain.Transfer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransferRepository extends MongoRepository<Transfer, String> {

    List<Transfer> findAllBySenderOrReceiverOrderByIdDesc(String sender, String receiver);

    void removeById(String id);
}
