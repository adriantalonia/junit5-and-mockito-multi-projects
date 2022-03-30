package com.junit5app.models;

import com.junit5app.exceptions.InsufficientMoneyException;

import java.math.BigDecimal;

public class Account {

    private String user;
    private BigDecimal money;
    private Bank bank;


    public Account(String user, BigDecimal money) {
        this.user = user;
        this.money = money;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void debit(BigDecimal amount) {
        BigDecimal newMoney = this.money.subtract(amount);
        if (newMoney.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientMoneyException("Insufficient Money");
        }
        this.money = newMoney;
    }

    public void credit(BigDecimal amount) {
        this.money = this.money.add(amount);
    }

    @Override
    public boolean equals(Object obj) {
        Account c = (Account) obj;
        if (!(obj instanceof Account)) {
            return false;
        }
        if (this.user == null || this.money == null) {
            return false;
        }
        return this.user.equals(c.getUser()) && this.money.equals(c.getMoney());
    }
}
