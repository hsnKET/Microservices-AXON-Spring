package me.ketlas.comptecqrses.commonapi.exceptions;

public class AmountNegativeException extends RuntimeException {

    public AmountNegativeException(){
        super("Amount should be not negative");
    }
}
