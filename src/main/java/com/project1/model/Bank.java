package com.project1.model;

import com.project1.annotations.Column;
import com.project1.annotations.Id;
import com.project1.annotations.Table;

@Table(name = "Bank")
public class Bank {

    @Id(name="routingNumber", dataType = "integer primary key")
    private int routingNumber;
    @Column(name = "userId", dataType = "integer references employee") //this is an example of creating a foreign key
    private int userId;// this will be a reference to the employee primary key
    @Column(name="bankName", dataType = "text")
    private String bankName;
    @Column(name="balance",dataType = "NUMERIC")
    private double balance;

    public Bank() {
    }

    public Bank(int routingNumber, int userId, String bankName, double balance) {
        this.routingNumber = routingNumber;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(int routingNumber) {
        this.routingNumber = routingNumber;
    }
}
