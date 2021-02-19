package com.uni.bankuni.service;

import com.uni.bankuni.domain.Transfer;
import com.uni.bankuni.domain.User;
import com.uni.bankuni.repository.TransferRepository;
import com.uni.bankuni.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;

    public List<Transfer> getHistory(String email) {
        User user = userRepository.findUserByEmail(email);

        return transferRepository.findAllBySenderOrReceiverOrderByIdDesc(user.getId(), user.getId());
    }
}
