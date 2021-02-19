package com.uni.bankuni.service;

import com.uni.bankuni.domain.Account;
import com.uni.bankuni.domain.User;
import com.uni.bankuni.repository.AccountRepository;
import com.uni.bankuni.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public void createAccount(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail);

        Account account = new Account();
        account.setOwner(user.getId());
        account.setAmount(0);
        account.setTransferAvailable(true);

        accountRepository.save(account);
    }

    public Account getAccount(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail);

        return accountRepository.findAccountByOwner(user.getId());
    }
}
