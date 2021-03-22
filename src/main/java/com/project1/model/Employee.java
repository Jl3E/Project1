package com.project1.model;

import com.project1.annotations.*;
import com.project1.annotations.Table;

@Table(name = "Employee")
public class Employee {
    @Id(name = "id", dataType = "Integer primary key")
    private int id;
    @Column(name ="name", dataType ="text")
    private String name;
    @Column(name = "salary", dataType ="float")
    private float salary;
    @Column(name = "designation", dataType ="text")
    private String designation;

    public Employee() {}

    public Employee(int id, String name, float salary, String designation) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.designation = designation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getClassName(){
        return this.getClass().getSimpleName();
    }
}
