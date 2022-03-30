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
