package stage.ifm.escqrsaxon.commands.aggregates;


import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import stage.ifm.escqrsaxon.commands.commands.AddAccountCommand;
import stage.ifm.escqrsaxon.commands.commands.CreditAccountCommand;
import stage.ifm.escqrsaxon.enums.AccountStatus;
import stage.ifm.escqrsaxon.events.AccountCreatedEvent;
import stage.ifm.escqrsaxon.events.AccountCreditedEvent;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private AccountStatus status;

    public AccountAggregate() {
    }

    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        log.info("####### AddAccountCommand Received #########");
        if (command.getInitialBalance() <= 0) throw new IllegalArgumentException("Balance must be positive");
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                AccountStatus.CREATED,
                command.getCurrency()
        ));
    }

    @CommandHandler
    public void handle(CreditAccountCommand command){
        log.info("############## CreditAccountCommand Received ##############");
        if (!status.equals(AccountStatus.ACTIVATED)) throw new RuntimeException("The account "+command.getId()+" is not activated");
        if (command.getAmount()<=0) throw new IllegalArgumentException("Amount must be positive");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        log.info("####### AccountCreatedEvent occured #########");
        this.accountId = event.getAccountId();
        this.balance = event.getInitialBalance();
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        log.info("####### AccountCreditedEvent occured #########");
        this.accountId = event.getAccountId();
        this.balance += event.getAmount();

    }
}
