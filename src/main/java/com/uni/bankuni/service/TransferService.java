package com.uni.bankuni.service;

import com.uni.bankuni.domain.Account;
import com.uni.bankuni.domain.Transfer;
import com.uni.bankuni.domain.TransferRequest;
import com.uni.bankuni.domain.User;
import com.uni.bankuni.exception.InsufficientAmountException;
import com.uni.bankuni.exception.TransferNotValidException;
import com.uni.bankuni.exception.UserNotFoundException;
import com.uni.bankuni.repository.AccountRepository;
import com.uni.bankuni.repository.TransferRepository;
import com.uni.bankuni.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public List<Transfer> getHistory(String email) {
        User user = userRepository.findUserByEmail(email);

        List<Transfer> transfers = transferRepository.findAllBySenderOrReceiverOrderByIdDesc(
                user.getId(), user.getId());

        for(Transfer transfer: transfers) {
            if(transfer.getSender().equals(user.getId())) {
                transfer.setType("Outgoing");
            } else {
                transfer.setType("Incoming");
            }
        }

        return transfers;
    }

    public Transfer verifyTransfer(TransferRequest transferRequest) throws
            UserNotFoundException,
            InsufficientAmountException {
        if (userRepository.findById(transferRequest.getReceiver()).isEmpty()) {
            throw new UserNotFoundException();
        } else {
            Account account = accountRepository.findAccountByOwner(transferRequest.getSender());

            if (account.getAmount() > 0 && account.getAmount() < transferRequest.getAmount()) {
                throw new InsufficientAmountException();
            } else {
                Transfer transfer = new Transfer();

                transfer.setAmount(transferRequest.getAmount());
                transfer.setMessage(transferRequest.getMessage());
                transfer.setReceiver(transferRequest.getReceiver());
                transfer.setSender(transferRequest.getSender());
                transfer.setType("None");
                transfer.setStartDate(new Date());
                transfer.setInProgress(true);

                transferRepository.save(transfer);

                return transfer;
            }
        }
    }

    public void executeTransfer(String transferId) throws InsufficientAmountException, TransferNotValidException {
        Transfer transfer = transferRepository.findById(transferId).orElse(null);

        if(transfer != null && transfer.isInProgress()) {
            Account senderAccount = accountRepository.findAccountByOwner(transfer.getSender());
            Account receiverAccount = accountRepository.findAccountByOwner(transfer.getReceiver());

            if (senderAccount.getAmount() < transfer.getAmount()) {
                throw new InsufficientAmountException();
            } else {
                senderAccount.setTransferAvailable(true);
                senderAccount.setAmount(senderAccount.getAmount() - transfer.getAmount());

                accountRepository.save(senderAccount);

                receiverAccount.setTransferAvailable(true);
                receiverAccount.setAmount(receiverAccount.getAmount() + transfer.getAmount());

                accountRepository.save(receiverAccount);

                transfer.setEndDate(new Date());
                transfer.setInProgress(false);

                transferRepository.save(transfer);
            }
        } else {
            throw new TransferNotValidException();
        }
    }
}
