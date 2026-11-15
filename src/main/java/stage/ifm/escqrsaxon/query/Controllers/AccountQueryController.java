package stage.ifm.escqrsaxon.query.Controllers;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import stage.ifm.escqrsaxon.query.dtos.AccountStatementResponseDTO;
import stage.ifm.escqrsaxon.query.entities.Account;
import stage.ifm.escqrsaxon.query.entities.AccountOperation;
import stage.ifm.escqrsaxon.query.queries.GetAccountStatementQuery;
import stage.ifm.escqrsaxon.query.queries.GetAllAccountsQuery;
import stage.ifm.escqrsaxon.query.queries.WatchEventQuery;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/accounts")
public class AccountQueryController {

    private QueryGateway  queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/all")
    public CompletableFuture<List<Account>> getAllAccount(){
        CompletableFuture<List<Account>> response = queryGateway.query(
                new GetAllAccountsQuery(),
                ResponseTypes.multipleInstancesOf(Account.class));
        return response;
    }

    @GetMapping("/accountStatement/{accountId}")
    public CompletableFuture<AccountStatementResponseDTO> getAccountStatement(@PathVariable String accountId) {
        return queryGateway.query(
                new GetAccountStatementQuery(accountId),
                ResponseTypes.instanceOf(AccountStatementResponseDTO.class));
    }

    @GetMapping(value = "/watch/{accountId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountOperation> watch(@PathVariable String accountId){
        SubscriptionQueryResult<AccountOperation, AccountOperation> result =
                queryGateway.subscriptionQuery(
                        new WatchEventQuery(accountId),
                        ResponseTypes.instanceOf(AccountOperation.class),
                        ResponseTypes.instanceOf(AccountOperation.class)
                );
        return result.initialResult().concatWith(result.updates());
    }
}
