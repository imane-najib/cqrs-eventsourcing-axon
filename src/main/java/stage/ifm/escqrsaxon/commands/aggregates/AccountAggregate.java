package stage.ifm.escqrsaxon.commands.aggregates;


import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import stage.ifm.escqrsaxon.commands.commands.AddAccountCommand;
import stage.ifm.escqrsaxon.commands.commands.CreditAccountCommand;
import stage.ifm.escqrsaxon.commands.commands.DebitAccountCommand;
import stage.ifm.escqrsaxon.commands.commands.UpdateStatusCommand;
import stage.ifm.escqrsaxon.enums.AccountStatus;
import stage.ifm.escqrsaxon.events.*;

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
        AggregateLifecycle.apply(new AccountActivatedEvent(
                command.getId(),
                AccountStatus.ACTIVATED
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

    @CommandHandler
    public void handle(DebitAccountCommand command){
        log.info("############## DebitAccountCommand Received ##############");
        if (!status.equals(AccountStatus.ACTIVATED)) throw new RuntimeException("The account "+command.getId()+" is not activated");
        if (command.getAmount()<=0) throw new IllegalArgumentException("Amount must be positive");
        if (this.balance<command.getAmount()) throw new IllegalArgumentException("insufficient balance");
        AggregateLifecycle.apply(new AccountDebitedEvent(
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
    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        log.info("####### AccountDebitedEvent occured #########");
        this.accountId = event.getAccountId();
        this.balance -= event.getAmount();

    }


    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        log.info("####### AccountActivatedEvent occured #########");
        this.accountId = event.getAccountId();
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(UpdateStatusCommand command){
        log.info("############## UpdateStatusCommand Received ##############");
        if (status.equals(command.getStatus())) throw new RuntimeException("This account "+command.getId()+" is alreeady "+command.getStatus());
        AggregateLifecycle.apply(new AccountUpdateStatusEvent(
                command.getId(),
                command.getStatus()
        ));
    }
    @EventSourcingHandler
    public void on(AccountUpdateStatusEvent event){
        log.info("############## AccountUpdateStatusEvent Occured ##############");
        this.accountId = event.getAccountId();
        this.status = event.getStatus();
    }
}
