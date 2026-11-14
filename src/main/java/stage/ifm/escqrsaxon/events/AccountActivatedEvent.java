package stage.ifm.escqrsaxon.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import stage.ifm.escqrsaxon.enums.AccountStatus;

@Getter @AllArgsConstructor
@NoArgsConstructor
public class AccountActivatedEvent {
    private String accountId;
    private AccountStatus status;

}
