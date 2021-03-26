package com.project1.orm;

import com.project1.model.Bank;
import com.project1.model.Car;
import com.project1.model.Employee;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class CustomTest {
    CustomPersistenceProvider cpp = new CustomPersistenceProvider();
    Car c = new Car(1,"honda",5000,true);

    @Test
    public void assertTrueTest(){
        Boolean actual = cpp.isNumeric(" 3jkl");
        Assert.assertEquals(false, actual);

    }

    @Test
    public void doesTableExistTest(){
        cpp.create(c);
        Boolean actual = cpp.doesTableExist(c);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void createTableTest(){
        cpp.dropTable(c);
        cpp.create(c);
        Boolean actual = cpp.doesTableExist(c);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void dropTableTest(){
        cpp.create(c);
        cpp.dropTable(c);
        Boolean actual = cpp.doesTableExist(c);
        Assert.assertEquals(false, actual);
    }

    @Test
    public void findByIdTestObject() throws ClassNotFoundException {
        Employee e3 = new Employee(3,"name",504,"dude");
        cpp.dropTable(e3);
        cpp.create(e3);
        cpp.insertInTable(e3);
        Bank b = new Bank(13452435,3,"Bank of America",3040);
        cpp.dropTable(b);
        cpp.create(b);
        cpp.insertInTable(b);
        Bank c3 = cpp.findById(b,13452435);
        String actual = c3.getBankName();
        Assert.assertEquals("Bank of America", actual);
    }

    @Test
    public void findByIdTestClass() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Employee e5 = new Employee(5,"name",504,"dude");
        cpp.dropTable(e5);
        cpp.create(e5);
        cpp.insertInTable(e5);
        Bank b = new Bank(516534435,5,"Bank of America",3040);
        cpp.dropTable(b);
        cpp.create(b);
        cpp.insertInTable(b);
        Bank c4 = cpp.findById(Bank.class,516534435);
        String actual = c4.getBankName();
        Assert.assertEquals("Bank of America", actual);
    }

    @Test
    public void insertTableTest() throws ClassNotFoundException {
        Employee e = new Employee(4,"bob",50,"hell");
        cpp.dropTable(e);
        cpp.create(e);
        cpp.insertInTable(e);
        Employee c2 = cpp.findById(e,4);
        String actual = c2.getName();
        Assert.assertEquals("bob", actual);
    }

    @Test
    public void updateTableTest() throws ClassNotFoundException {
        cpp.create(c);
        cpp.insertInTable(c);
        Car car = cpp.findById(c,1);
        car.setPreOwned(false);
        Boolean actual = car.getPreOwned();
        Assert.assertEquals(false, actual);
    }

//----------------------------------------------------cache---------------------------------------------------------
    @Test
    public void createTableCacheTest(){
        cpp.setCacheOn();
        cpp.dropTable(c);
        cpp.create(c);
        Boolean actual = cpp.doesTableExist(c);
        Assert.assertEquals(true, actual);
    }

    @Test
    public void dropTableCacheTest(){
        cpp.setCacheOn();
        cpp.create(c);
        cpp.dropTable(c);
        Boolean actual = cpp.doesTableExist(c);
        Assert.assertEquals(false, actual);
    }

    @Test
    public void findByIdCacheTestObject() throws ClassNotFoundException {
        cpp.setCacheOn();
        Employee e3 = new Employee(3,"name",504,"dude");
        cpp.dropTable(e3);
        cpp.create(e3);
        cpp.insertInTable(e3);
        Bank b = new Bank(13452435,3,"Bank of America",3040);
        cpp.dropTable(b);
        cpp.create(b);
        cpp.insertInTable(b);
        Bank c3 = cpp.findById(b,13452435);
        String actual = c3.getBankName();
        Assert.assertEquals("Bank of America", actual);
    }

    @Test
    public void findByIdCacheTestClass() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        cpp.setCacheOn();
        Employee e5 = new Employee(5,"name",504,"dude");
        cpp.dropTable(e5);
        cpp.create(e5);
        cpp.insertInTable(e5);
        Bank b = new Bank(516534435,5,"Bank of America",3040);
        cpp.dropTable(b);
        cpp.create(b);
        cpp.insertInTable(b);
        Bank c4 = cpp.findById(Bank.class,516534435);
        String actual = c4.getBankName();
        Assert.assertEquals("Bank of America", actual);
    }

    @Test
    public void insertCacheTableTest() throws ClassNotFoundException {
        cpp.setCacheOn();
        Employee e = new Employee(4,"bob",50,"hell");
        cpp.dropTable(e);
        cpp.create(e);
        cpp.insertInTable(e);
        Employee c2 = cpp.findById(e,4);
        String actual = c2.getName();
        Assert.assertEquals("bob", actual);
    }

    @Test
    public void updateCacheTableTest() throws ClassNotFoundException {
        cpp.setCacheOn();
        cpp.create(c);
        cpp.insertInTable(c);
        Car car = cpp.findById(c,1);
        car.setPreOwned(false);
        Boolean actual = car.getPreOwned();
        Assert.assertEquals(false, actual);
    }

}
