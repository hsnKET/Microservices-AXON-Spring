package me.ketlas.comptecqrses.query.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ketlas.comptecqrses.commonapi.queries.GetAccountByIdQuery;
import me.ketlas.comptecqrses.commonapi.queries.GetAllAccountsQuery;
import me.ketlas.comptecqrses.query.entities.Account;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/query/account")
public class AccountQueryController {

    private QueryGateway queryGateway;
    @GetMapping("/all")
    public CompletableFuture<List<Account>> allAccount(){
        return queryGateway.query(
                new GetAllAccountsQuery(),
                ResponseTypes.multipleInstancesOf(Account.class)
        );
    }

    @GetMapping("/byId/{id}")
    public CompletableFuture<Account> accountById(String id){
        return queryGateway.query(
                new GetAccountByIdQuery(id),
                ResponseTypes.instanceOf(Account.class)
        );
    }

}
