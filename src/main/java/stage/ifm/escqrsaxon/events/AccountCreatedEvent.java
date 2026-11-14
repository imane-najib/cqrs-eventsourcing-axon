package stage.ifm.escqrsaxon.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import stage.ifm.escqrsaxon.enums.AccountStatus;

@Getter @AllArgsConstructor
@NoArgsConstructor
public class AccountCreatedEvent {
    private String accountId;
    private double initialBalance;
    private AccountStatus status;
    private String currency;
}
