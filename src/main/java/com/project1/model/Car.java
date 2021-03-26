package com.project1.model;

import com.project1.annotations.*;
import com.project1.annotations.Table;

@Table(name = "car")
public class Car {
    @Id(name = "carId", dataType = "Integer primary key")
    private int carId;
    @Column(name = "name", dataType ="text")
    private String name;
    @Column(name = "price", dataType ="NUMERIC")
    private double price;
    @Column(name = "preOwned", dataType ="boolean")
    private boolean preOwned;

    public Car() {}

    public Car(int carId,String name, double price, boolean preOwned) {
        this.carId = carId;
        this.name = name;
        this.price = price;
        this.preOwned = preOwned;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean getPreOwned() {
        return preOwned;
    }

    public void setPreOwned(boolean preOwned) {
        this.preOwned = preOwned;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
