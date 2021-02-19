package com.uni.bankuni.aspect;

import com.uni.bankuni.domain.Account;
import com.uni.bankuni.domain.Transfer;
import com.uni.bankuni.repository.AccountRepository;
import com.uni.bankuni.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicReference;

@Aspect
@Configuration
@RequiredArgsConstructor
public class TransferServiceAspect {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    @Around(value = "execution(* com.uni.bankuni.service.TransferService.executeTransfer(String)) && args(transferId)",
            argNames = "pjp,transferId")
    public Object aroundExecuteTransfer(ProceedingJoinPoint pjp, String transferId) throws Throwable {
        Transfer transfer = transferRepository.findById(transferId).orElse(null);

        if (transfer != null) {
            AtomicReference<Account> senderAccount = new AtomicReference<>(
                    accountRepository.findAccountByOwner(transfer.getSender()));
            AtomicReference<Account> receiverAccount = new AtomicReference<>(
                    accountRepository.findAccountByOwner(transfer.getReceiver()));

            if (senderAccount.get().isTransferAvailable() && receiverAccount.get().isTransferAvailable()) {
                pjp.proceed();
            } else {
                while (!senderAccount.get().isTransferAvailable() || !receiverAccount.get().isTransferAvailable()) {
                    senderAccount.set(accountRepository.findAccountByOwner(transfer.getSender()));
                    receiverAccount.set(accountRepository.findAccountByOwner(transfer.getReceiver()));
                    Thread.sleep(2000);
                }

                pjp.proceed();
            }
        }

        return null;
    }
}
