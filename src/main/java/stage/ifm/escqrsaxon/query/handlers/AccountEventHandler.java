package stage.ifm.escqrsaxon.query.handlers;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.springframework.stereotype.Component;
import stage.ifm.escqrsaxon.events.AccountCreatedEvent;
import stage.ifm.escqrsaxon.events.AccountCreditedEvent;
import stage.ifm.escqrsaxon.query.entities.Account;
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
        log.info("================== Query Side AccountCreatedEvent Recived =========================");
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
}
