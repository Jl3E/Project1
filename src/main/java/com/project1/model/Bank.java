package com.project1.model;

import com.project1.annotations.Column;
import com.project1.annotations.Id;
import com.project1.annotations.Table;

@Table(name = "Bank")
public class Bank {

    @Id(name="routingNumber", dataType = "int primary key")
    private int routingNumber;
    @Column(name = "userId", dataType = "Integer references Employee") //this is an example of creating a foreign key
    private int userId;// this will be a reference to the employee primary key
    @Column(name="bankName", dataType = "text")
    private String bankName;
    @Column(name="balance",dataType = "float")
    private float balance;

    public Bank() {
    }

    public Bank(int userId, String bankName, float balance) {
        this.userId = userId;
        this.bankName = bankName;
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
