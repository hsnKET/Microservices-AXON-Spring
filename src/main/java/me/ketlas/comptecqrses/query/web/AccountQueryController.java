package me.ketlas.comptecqrses.query.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ketlas.comptecqrses.commonapi.queries.GetAllAccountsQuery;
import me.ketlas.comptecqrses.query.entities.Account;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@AllArgsConstructor
public class AccountQueryController {

    private QueryGateway queryGateway;
    public CompletableFuture<Account> allAccount(){
        return queryGateway.query(
                new GetAllAccountsQuery(),
                ResponseTypes.instanceOf(Account.class)
        );
    }

}
