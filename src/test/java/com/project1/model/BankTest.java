package com.project1.model;

import org.junit.Assert;
import org.junit.Test;

public class BankTest {

    Bank b = new Bank();

    @Test
    public void setNameTest(){
        b.setBankName("Chase");
        Assert.assertEquals("Chase",b.getBankName());
    }


    @Test
    public void getNameTest(){
        b.setBankName("Chase");
        Assert.assertEquals("Chase",b.getBankName());
    }
}
