package me.ketlas.comptecqrses.commands.aggregates;

import me.ketlas.comptecqrses.commonapi.commands.CreateAccountCommand;
import me.ketlas.comptecqrses.commonapi.commands.CreditAccountCommand;
import me.ketlas.comptecqrses.commonapi.commands.DebitAccountCommand;
import me.ketlas.comptecqrses.commonapi.enums.AccountStatus;
import me.ketlas.comptecqrses.commonapi.events.AccountActivatedEvent;
import me.ketlas.comptecqrses.commonapi.events.AccountCreatedEvent;
import me.ketlas.comptecqrses.commonapi.events.AccountCreditedEvent;
import me.ketlas.comptecqrses.commonapi.events.AccountDebitedEvent;
import me.ketlas.comptecqrses.commonapi.exceptions.AmountNegativeException;
import me.ketlas.comptecqrses.commonapi.exceptions.InsufficientBalanceException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate(){
        // Required by AXON
    }


    // this decision function
    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand){
        if (createAccountCommand.getInitialBalance() < 0)
            throw new RuntimeException("Impossible to create account with negative balance");
        //TODO: implement something else here...

        //emit the event
        AggregateLifecycle.apply(new AccountCreatedEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency(),
                AccountStatus.CREATED));
    }

    //event sourcing handler: or evaluation function
    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        //update state of application here
        this.accountId = event.getId();
        this.balance = event.getInitialBalance();
        this.status = AccountStatus.CREATED;
        this.currency = event.getCurrency();
        //emit another event
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));

    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event){
        //update state of application here
        this.accountId = event.getId();
        this.status = event.getStatus();

    }


    @CommandHandler
    public void handle(CreditAccountCommand command){

        if(command.getAmount() < 0)
            throw new AmountNegativeException();

        AggregateLifecycle.apply(
                new AccountCreditedEvent(
                        command.getId(),
                        command.getAmount(),
                        command.getCurrency(),
                        status)
        );
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        this.balance += event.getAmount();
    }


    @CommandHandler
    public void handle(DebitAccountCommand command){

        if(command.getAmount() > this.balance)
            throw new InsufficientBalanceException();

        AggregateLifecycle.apply(
                new AccountDebitedEvent(
                        command.getId(),
                        command.getAmount(),
                        command.getCurrency()
                )
        );
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        this.balance -= event.getAmount();
    }



}
