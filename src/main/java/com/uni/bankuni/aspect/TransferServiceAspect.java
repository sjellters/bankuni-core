package com.uni.bankuni.aspect;

import com.uni.bankuni.domain.Account;
import com.uni.bankuni.domain.Transfer;
import com.uni.bankuni.repository.AccountRepository;
import com.uni.bankuni.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Aspect
@Configuration
@RequiredArgsConstructor
public class TransferServiceAspect {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    @AfterReturning(pointcut = "execution(* com.uni.bankuni.service.TransferService.verifyTransfer(..))",
            returning = "returnedTransfer")
    public void afterReturnedTransfer(Transfer returnedTransfer) {
        AtomicReference<Account> senderAccount = new AtomicReference<>(
                accountRepository.findAccountByOwner(returnedTransfer.getSender()));
        AtomicReference<Account> receiverAccount = new AtomicReference<>(
                accountRepository.findAccountByOwner(returnedTransfer.getReceiver()));

        senderAccount.get().setTransferAvailable(false);
        receiverAccount.get().setTransferAvailable(false);

        accountRepository.save(senderAccount.get());
        accountRepository.save(receiverAccount.get());

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }

            Transfer updatedTransfer = transferRepository.findById(returnedTransfer.getId()).orElse(null);

            if (updatedTransfer != null && updatedTransfer.isInProgress()) {
                senderAccount.set(accountRepository.findAccountByOwner(returnedTransfer.getSender()));
                receiverAccount.set(accountRepository.findAccountByOwner(returnedTransfer.getReceiver()));

                senderAccount.get().setTransferAvailable(true);
                receiverAccount.get().setTransferAvailable(true);

                accountRepository.save(senderAccount.get());
                accountRepository.save(receiverAccount.get());

                transferRepository.removeById(updatedTransfer.getId());
            }
        });
    }

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
