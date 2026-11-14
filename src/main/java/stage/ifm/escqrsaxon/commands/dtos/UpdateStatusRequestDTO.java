package stage.ifm.escqrsaxon.commands.dtos;

import stage.ifm.escqrsaxon.enums.AccountStatus;

public record UpdateStatusRequestDTO(String accountId, AccountStatus status) {
}
