package com.uni.bankuni.repository;

import com.uni.bankuni.domain.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {

    Account findAccountByOwner(String owner);
}
