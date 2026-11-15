package stage.ifm.escqrsaxon.query.dtos;

import stage.ifm.escqrsaxon.query.entities.Account;
import stage.ifm.escqrsaxon.query.entities.AccountOperation;

import java.util.List;

public record AccountStatementResponseDTO (Account account, List<AccountOperation> operations){
}
