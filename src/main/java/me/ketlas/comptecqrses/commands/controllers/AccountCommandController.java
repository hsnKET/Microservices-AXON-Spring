package me.ketlas.comptecqrses.commands.controllers;

import lombok.AllArgsConstructor;
import me.ketlas.comptecqrses.commonapi.commands.CreateAccountCommand;
import me.ketlas.comptecqrses.commonapi.commands.CreditAccountCommand;
import me.ketlas.comptecqrses.commonapi.commands.DebitAccountCommand;
import me.ketlas.comptecqrses.commonapi.dtos.CreateAccountRequestDTO;
import me.ketlas.comptecqrses.commonapi.dtos.CreditAccountRequestDTO;
import me.ketlas.comptecqrses.commonapi.dtos.DebitAccountRequestDTO;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/commands/account")
@AllArgsConstructor
public class AccountCommandController {

    private CommandGateway commandGateway;
    private EventStore eventStore;

    @PostMapping("/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO request){

        // will return account ID of type String
        CompletableFuture<String> sendCommandResponse = commandGateway
                .send(new CreateAccountCommand(UUID.randomUUID().toString(),
                request.getInitialBalance(),
                request.getCurrency()
        ));

        return  sendCommandResponse;
    }


    @PutMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request){

        CompletableFuture<String> sendCommandResponse = commandGateway.send(new CreditAccountCommand(request.getId(),
                request.getAmount(),
                request.getCurrency()
        ));

        return  sendCommandResponse;
    }

    @PutMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request){

        CompletableFuture<String> sendCommandResponse =
                commandGateway.send(new DebitAccountCommand(request.getId(),
                request.getAmount(),
                request.getCurrency()
        ));

        return  sendCommandResponse;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception exception){
        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/eventSotre/{accountId}")
    public Stream eventStore(@PathVariable String accountId){
        return eventStore.readEvents(accountId).asStream();
    }
}
