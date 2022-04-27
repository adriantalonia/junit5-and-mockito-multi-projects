package com.springboottestapp.springboot_test_app.models;

import java.math.BigDecimal;

public class TransactionDTO {
    private Long originAccount;
    private Long destinyAccount;
    private BigDecimal amount;
    private Long bankId;

    public Long getOriginAccount() {
        return originAccount;
    }

    public void setOriginAccount(Long originAccount) {
        this.originAccount = originAccount;
    }

    public Long getDestinyAccount() {
        return destinyAccount;
    }

    public void setDestinyAccount(Long destinyAccount) {
        this.destinyAccount = destinyAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }
}
