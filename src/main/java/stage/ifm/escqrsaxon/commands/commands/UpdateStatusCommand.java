package stage.ifm.escqrsaxon.commands.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import stage.ifm.escqrsaxon.enums.AccountStatus;

@Getter @AllArgsConstructor
public class UpdateStatusCommand {
    @TargetAggregateIdentifier
    private String id;
    private AccountStatus  status;
}
