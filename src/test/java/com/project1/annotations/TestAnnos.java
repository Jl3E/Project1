package com.project1.annotations;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project1.config.DataSource;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class TestAnnos {

    @Table(name = "TableAnnotationTest")
    class  TableAnnotationTest{

        @Id(name = "aname",dataType = "String")
        String value = "a Value";
        @Column(name = "something", dataType = "something again")
        String anotherValue = "a value again";

        Field[] fieldsO1 = this.getClass().getDeclaredFields();
        Queue<String> id = new LinkedList<>();

    }
    @Test
    public void idTest(){
        TableAnnotationTest tt = new TableAnnotationTest();

        for(Field f: tt.fieldsO1){
            if (f.getAnnotation(com.project1.annotations.Id.class) != null) {
                tt.id.add(f.getAnnotation(com.project1.annotations.Id.class).name());
            }
        }
        Assert.assertEquals("aname",tt.id.poll());
    }

    @Test
    public void columnTest(){
        TableAnnotationTest tt = new TableAnnotationTest();

        for(Field f: tt.fieldsO1){
            if (f.getAnnotation(com.project1.annotations.Column.class) != null) {
                tt.id.add(f.getAnnotation(com.project1.annotations.Column.class).name());
            }
        }
        Assert.assertEquals("something",tt.id.poll());
    }

    @Test
    public void tableTest(){
        TableAnnotationTest tt = new TableAnnotationTest();
        String tables = tt.getClass().getAnnotation(com.project1.annotations.Table.class).name();
        Assert.assertEquals("TableAnnotationTest",tables);
    }
}
