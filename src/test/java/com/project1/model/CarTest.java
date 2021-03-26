package com.project1.model;

import org.junit.Assert;
import org.junit.Test;

public class CarTest {
    Car c = new Car();

    @Test
    public void setNameTest(){
        c.setName("car");
        Assert.assertEquals("car",c.getName());
    }


    @Test
    public void getNameTest(){
        c.setName("car");
        Assert.assertEquals("car",c.getName());
    }
}
