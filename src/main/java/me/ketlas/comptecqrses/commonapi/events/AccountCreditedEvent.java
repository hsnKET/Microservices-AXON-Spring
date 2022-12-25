package me.ketlas.comptecqrses.commonapi.events;

import lombok.Getter;
import me.ketlas.comptecqrses.commonapi.enums.AccountStatus;

public class AccountCreditedEvent extends BaseEvent<String> {

    @Getter private double amount;
    @Getter private String currency;
    @Getter private AccountStatus status;

    public AccountCreditedEvent(String id, double amount, String currency, AccountStatus status) {
        super(id);
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }
}
