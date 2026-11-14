package stage.ifm.escqrsaxon.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @AllArgsConstructor
@NoArgsConstructor
public class AccountDebitedEvent {
    private String accountId;
    private double amount;
    private String currency;

}
