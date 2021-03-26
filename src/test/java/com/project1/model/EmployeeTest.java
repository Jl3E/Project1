package com.project1.model;

import org.junit.Assert;
import org.junit.Test;

public class EmployeeTest {

    Employee e = new Employee();

    @Test
    public void setNameTest(){
        e.setName("joe");
        Assert.assertEquals("joe",e.getName());
    }


    @Test
    public void getNameTest(){
        e.setName("joe");
        Assert.assertEquals("joe",e.getName());
    }
}
