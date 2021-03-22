package com.project1.model;

import com.project1.annotations.*;
import com.project1.annotations.Table;

@Table(name = "car")
public class Car {
    @Id(name = "carId", dataType = "Integer primary key")
    private int carId;
    @Column(name = "name", dataType ="text")
    private String name;
    @Column(name = "price", dataType ="float")
    private float price;
    @Column(name = "preOwned", dataType ="boolean")
    private boolean preOwned;

    public Car() {}

    public Car(int carId,String name, float price, boolean preOwned) {
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isPreOwned() {
        return preOwned;
    }

    public void setPreOwned(boolean preOwned) {
        this.preOwned = preOwned;
    }
}
