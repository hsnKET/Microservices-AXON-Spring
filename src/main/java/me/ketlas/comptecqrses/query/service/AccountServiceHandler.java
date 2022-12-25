package me.ketlas.comptecqrses.query.service;

import lombok.AllArgsConstructor;
import me.ketlas.comptecqrses.commonapi.enums.OperationType;
import me.ketlas.comptecqrses.commonapi.events.AccountActivatedEvent;
import me.ketlas.comptecqrses.commonapi.events.AccountCreatedEvent;
import me.ketlas.comptecqrses.commonapi.events.AccountCreditedEvent;
import me.ketlas.comptecqrses.commonapi.events.AccountDebitedEvent;
import me.ketlas.comptecqrses.query.entities.Account;
import me.ketlas.comptecqrses.query.entities.Operation;
import me.ketlas.comptecqrses.query.repositories.AccountRepository;
import me.ketlas.comptecqrses.query.repositories.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceHandler {

    private AccountRepository accountRepository;
    private OperationRepository operationRepository;


    @EventHandler
    public void on(AccountCreatedEvent event){
        Account account = new Account();
        account.setId(event.getId());
        account.setCurrency(event.getCurrency());
        account.setBalance(event.getInitialBalance());
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent event){
        Account account = accountRepository.findById(event.getId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent event){
        Account account = accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAmount(event.getAmount());
        operation.setType(OperationType.CREDIT);
        operation.setDate(new Date());//FIXME: set date when we want to emit the event
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance() - event.getAmount());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent event){
        Account account = accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAmount(event.getAmount());
        operation.setType(OperationType.DEBIT);
        operation.setDate(new Date());//FIXME: set date when we want to emit the event
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance() + event.getAmount());
        accountRepository.save(account);
    }

}
