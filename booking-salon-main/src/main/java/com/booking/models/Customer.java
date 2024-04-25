package com.booking.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer extends Person {
    private double wallet;
    private Membership member;

    private double newBalance;

    public void setWalletBalance(double newBalance) {
        this.wallet = wallet;
        this.newBalance = newBalance;
    }

    public double getWallet(){
        return wallet;
    }

    public double balance(double reservationPrice){
        return wallet - reservationPrice;
    }
}
