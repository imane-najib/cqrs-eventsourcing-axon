package stage.ifm.escqrsaxon.query.handlers;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import stage.ifm.escqrsaxon.query.dtos.AccountStatementResponseDTO;
import stage.ifm.escqrsaxon.query.entities.Account;
import stage.ifm.escqrsaxon.query.entities.AccountOperation;
import stage.ifm.escqrsaxon.query.queries.GetAccountStatementQuery;
import stage.ifm.escqrsaxon.query.queries.GetAllAccountsQuery;
import stage.ifm.escqrsaxon.query.queries.WatchEventQuery;
import stage.ifm.escqrsaxon.query.repositories.AccountRepository;
import stage.ifm.escqrsaxon.query.repositories.OperationRepository;

import java.util.List;

@Component
public class AccountQueryHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    public AccountQueryHandler(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }
    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query){
        return accountRepository.findAll();
    }

    @QueryHandler
    public AccountStatementResponseDTO on(GetAccountStatementQuery query){
        Account account = accountRepository.findById(query.getAccountId()).get();
        List<AccountOperation> operations = operationRepository.findByAccountId(query.getAccountId());
        return new AccountStatementResponseDTO(account, operations);
    }

    @QueryHandler
    public AccountOperation on(WatchEventQuery query){
        return AccountOperation.builder().build();
    }
}
