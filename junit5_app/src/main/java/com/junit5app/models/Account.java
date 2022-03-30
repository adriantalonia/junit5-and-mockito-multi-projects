package com.junit5app.models;

import java.math.BigDecimal;

public class Account {

    private String user;
    private BigDecimal money;

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
}
