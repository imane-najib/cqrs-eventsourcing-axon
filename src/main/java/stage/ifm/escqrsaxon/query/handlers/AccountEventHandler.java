package stage.ifm.escqrsaxon.query.handlers;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Component;
import stage.ifm.escqrsaxon.enums.OperationType;
import stage.ifm.escqrsaxon.events.*;
import stage.ifm.escqrsaxon.query.entities.Account;
import stage.ifm.escqrsaxon.query.entities.AccountOperation;
import stage.ifm.escqrsaxon.query.repositories.AccountRepository;
import stage.ifm.escqrsaxon.query.repositories.OperationRepository;

@Slf4j
@Component
public class AccountEventHandler {
    private OperationRepository operationRepository;
    private AccountRepository accountRepository;

    public AccountEventHandler(OperationRepository operationRepository, AccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event, EventMessage eventMessage) {
        log.info("================== Query Side AccountCreatedEvent Received =========================");
        Account account = Account
                .builder()
                .id(event.getAccountId())
                .balance(event.getInitialBalance())
                .status(event.getStatus())
                .currency(event.getCurrency())
                .createdAt(eventMessage.getTimestamp())
                .build();
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent event){
        log.info("================ Query Side AccountActivatedEvent Received ================");
        Account account = accountRepository.findById(event.getAccountId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountUpdateStatusEvent event){
        log.info("================ Query Side AccountUpdateStatusEvent Received ================");
        Account account = accountRepository.findById(event.getAccountId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent event, EventMessage eventMessage){
        log.info("================ Query Side AccountDebitedEvent Received ================");
        Account account = accountRepository.findById(event.getAccountId()).get();
        AccountOperation accountOperation = AccountOperation.builder()
                .amount(event.getAmount())
                .date(eventMessage.getTimestamp())
                .type(OperationType.DEBIT)
                .currency(event.getCurrency())
                .account(account)
                .build();
        operationRepository.save(accountOperation);
        account.setBalance(account.getBalance() - accountOperation.getAmount());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent event, EventMessage eventMessage){
        log.info("================ Query Side AccountCreditedEvent Received ================");
        Account account = accountRepository.findById(event.getAccountId()).get();
        AccountOperation accountOperation = AccountOperation.builder()
                .amount(event.getAmount())
                .date(eventMessage.getTimestamp())
                .type(OperationType.CREDIT)
                .currency(event.getCurrency())
                .account(account)
                .build();
        operationRepository.save(accountOperation);
        account.setBalance(account.getBalance() + accountOperation.getAmount());
        accountRepository.save(account);

    }
}
