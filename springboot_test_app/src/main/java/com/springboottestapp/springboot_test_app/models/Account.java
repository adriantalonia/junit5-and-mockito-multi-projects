package com.springboottestapp.springboot_test_app.models;

import com.springboottestapp.springboot_test_app.exceptions.InsufficientMoneyException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal money;

    public Account() {
    }

    public Account(Long id, String name, BigDecimal money) {
        this.id = id;
        this.name = name;
        this.money = money;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(name, account.name) && Objects.equals(money, account.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, money);
    }
}
