package me.ketlas.comptecqrses.commonapi.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(){
        super("Balance insufficient exception");
    }
}
