package stage.ifm.escqrsaxon.commands.aggregates;


import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import stage.ifm.escqrsaxon.commands.commands.AddAccountCommand;
import stage.ifm.escqrsaxon.enums.AccountStatus;
import stage.ifm.escqrsaxon.events.AccountCreatedEvent;

@Aggregate
public class AccountAggregate {
    private String accountId;
    private double balance;
    private AccountStatus status;

    public AccountAggregate() {
    }

    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        if (command.getInitialBalance() <= 0) throw new IllegalArgumentException("Balance must be positive");
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                AccountStatus.CREATED,
                command.getCurrency()

        ));
    }
}
