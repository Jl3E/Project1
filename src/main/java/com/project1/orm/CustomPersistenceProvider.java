package com.project1.orm;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project1.annotations.Column;
import com.project1.config.ConnectionUtil;
import com.project1.model.Car;


import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

public class CustomPersistenceProvider {

    public CustomPersistenceProvider() {

    }

    public void persist(Object o) {

        if(doesTableExist(o)){
            System.out.println("Table exists alright");

        }else{
            String sql = createTableSQL(o);

            try{
                Statement st = ConnectionUtil.getInstance().getConnection().createStatement();
                int i = st.executeUpdate(sql);
                System.out.println("The number of updated rows were " + i);

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    public boolean doesTableExist(Object o) {//TODO FIX THIS, NOT WORKING NOW, fixed! needed to have the table saved to lowercase.
        String tableNameCheck = o.getClass().getSimpleName().toLowerCase();//the name of the tables will be the model class names.

        try {
            Connection con = ConnectionUtil.getInstance().getConnection();

            DatabaseMetaData meta = con.getMetaData();
            ResultSet res = meta.getTables(null, null, null,
                    new String[]{"TABLE"});
            while (res.next()) {
                if (tableNameCheck.equals(res.getString("TABLE_NAME"))) {
                    System.out.println("Object table already exists.");// BreadCrumb to test if boolean switches
                    return true;
                }
//                System.out.println("in while loop");//bread crumbs
            }
            res.close();

            con.close();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        return false;
    }

    public String createTableSQL(Object o){
        Queue<String> columns = new LinkedList<>();//this holds the table names and data types

        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String className = o.getClass().getSimpleName(); // this gives the classname to be used as the table name

        //this get my annotations from the class of Object o passed and the annotations .name() gives the column name and .dataType() gives the sql data type acceptable for user
        for(Field f: fields){
            if(f.getAnnotation(com.project1.annotations.Id.class) != null){
                columns.add(f.getAnnotation(com.project1.annotations.Id.class).name());
                columns.add(f.getAnnotation(com.project1.annotations.Id.class).dataType());
            }
            if(f.getAnnotation(com.project1.annotations.Column.class) != null) {
                columns.add(f.getAnnotation(Column.class).name());
                columns.add(f.getAnnotation(Column.class).dataType());
            }
        }

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("create table ");
        sqlBuilder.append(className.toLowerCase()+"(");
        int size = columns.size()/2;

        for(int i=0; i < size; i++){
            if (columns.size() == 2){ // this is hard coded as 2 because the last two elements in the queue will be the column name and data type
                sqlBuilder.append(columns.poll()+" "+columns.poll()+");");
                break;
            }
            sqlBuilder.append(columns.poll()+" "+columns.poll()+", ");
        }
        //System.out.println(sqlBuilder); // to test the database with first

        String sql = String.valueOf(sqlBuilder);
//        System.out.println(sql);//TODO: THIS IS THE RIGHT SQL
        return sql;
    }

    public void insertInTable(Object o){
//        Queue<String> data = new LinkedList<>();//this holds the table names and data types

        Class clazz = o.getClass();
        String className = o.getClass().getSimpleName(); // this gives the classname to be used as the table name

        Gson gson = new Gson();
        String json = gson.toJson(o);
        String yourJson = json;
        JsonElement element = new JsonParser().parse(json);
        JsonObject obj = element.getAsJsonObject(); //since you know it's a JsonObject
        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();//will return members of your object

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("insert into ");
        sqlBuilder.append(className.toLowerCase()+" values (");
        int size = entries.size();
        int indexValues = 0;
        for (Map.Entry<String, JsonElement> entry: entries) {
            String check = String.valueOf(entry.getValue());
            if(check.contains("\"")){
//                System.out.println("yea it contains \" what about it? HUH?!"); // needed to replace (")'s w/ (')'s
                String newCheck = check.replace("\"","\'");
//                System.out.println(newCheck);
                //this if loop is used to enter strings into sql with single quotes compared to double quotes.
                if (indexValues == entries.size()-1){ // this is hard coded as 1 because the last value should close the statement
                    sqlBuilder.append(newCheck+");");
                    break;
                }
                sqlBuilder.append(newCheck+", ");
                indexValues++;
                continue;  // this is need to skip over the other ford with quotes being added to the StringBuilder
            }
            if (indexValues == entries.size()-1){ // this is hard coded as 1 because the last value should close the statement
                sqlBuilder.append(entry.getValue()+");");
                break;
            }
            sqlBuilder.append(entry.getValue()+", ");
            indexValues++;
        }

        String sql = String.valueOf(sqlBuilder);
//        System.out.println(sql);
        try{
            Statement st = ConnectionUtil.getInstance().getConnection().createStatement();
            int i = st.executeUpdate(sql);
            System.out.println("The number of updated rows were " + i);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void selectAllSql(Object o){
        String className = o.getClass().getSimpleName().toLowerCase();
        String sql = "select * from "+ className+";";

//        if(doesTableExist(o)){
            try{
                Statement st = ConnectionUtil.getInstance().getConnection().createStatement();
                ResultSet resultSet = st.executeQuery(sql);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = resultSet.getString(i);
//                    System.out.print(columnValue + " " + rsmd.getColumnName(i));// gets column names too
                        System.out.print(columnValue);
                    }
                    System.out.println("");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
//        }else{
//            System.out.println("Your table doesn't exist bud.");
//        }
        //TODO: Fix if/else if needed

    }

    public void dropTable(Object o){
        String tableName = o.getClass().getSimpleName().toLowerCase();
        String sql = "drop table "+tableName+";";
        try{
            Statement st = ConnectionUtil.getInstance().getConnection().createStatement();
            int i = st.executeUpdate(sql);
            System.out.println("The number of updated rows were " + i);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void findById(Object o, int primaryKey){
        String tableName = o.getClass().getSimpleName().toLowerCase();
        Queue<String> columns = new LinkedList<>();
        Queue<String> id = new LinkedList<>();
        Queue<String> values = new LinkedList<>();
        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field f: fields){
            if(f.getAnnotation(com.project1.annotations.Id.class) != null){
                id.add(f.getAnnotation(com.project1.annotations.Id.class).name());
                columns.add(f.getAnnotation(com.project1.annotations.Id.class).name());
            }
            if(f.getAnnotation(com.project1.annotations.Column.class) != null) {
                columns.add(f.getAnnotation(Column.class).name());
            }
        }

        String sql = "select * from "+tableName+" where "+id.poll()+"="+primaryKey+";";
        try{
            Statement st = ConnectionUtil.getInstance().getConnection().createStatement();
            ResultSet resultSet = st.executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
//                    if (i > 1) System.out.print(",  ");
//                    String columnValue = resultSet.getString(i);
                    values.add(resultSet.getString(i));
//                    System.out.print(columnValue + " " + rsmd.getColumnName(i));// gets column names too
//                    System.out.print(columnValue); // saves the output as a string but i need it in a collection
//                    System.out.println(values); // this shows that i've gotten the data for the id
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        int size = columns.size();
        for(int i=0;i<size;i++){
            if(values.size() == 1){
                if(isNumeric(values.peek())) {
                    jsonBuilder.append("\"" + columns.poll() + "\":"+values.poll()+"}");
                    continue;
                }else{
                    jsonBuilder.append("\"" + columns.poll() + "\":\""+values.poll()+"\"}");
                    continue;
                }
            }
            if(isNumeric(values.peek())) {
                jsonBuilder.append("\"" + columns.poll() + "\":"+values.poll()+",");
                continue;
            }else{
                jsonBuilder.append("\"" + columns.poll() + "\":\""+values.poll()+"\",");
                continue;
            }
        }
        System.out.println(jsonBuilder);
        String employeeJson = "{\"name\":\"sam\",\"id\":3,\"salary\":50.321,\"designation\":\"Alpha\"}";
//        System.out.println(employeeJson);
        //need to make a string like this with my data
//        Class clazz = o.getClass();
//        Field[] fields = clazz.getDeclaredFields();
//        String className = o.getClass().getSimpleName();
//
//        Gson gson = new Gson();
//        clazz obj = gson.fromJson(jsonBuilder,o)
//        o.getClass() o = gson.fromJson(jsonBuilder,o.getClass())
//        Employee employee1 = gson.fromJson(employeeJson, Employee.class);
//TODO: either return a json or figure out how to put that into something can't make it into an classType of object


//        return object;
    } //TODO: delete this method and update test to be this method name

    public Object findByIdTest(Class c, int primaryKey) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String tableName = c.getSimpleName().toLowerCase();
        System.out.println(tableName);
        Queue<String> columns = new LinkedList<>();
        Queue<String> id = new LinkedList<>();
        Queue<String> values = new LinkedList<>();
        Field[] fields = c.getDeclaredFields();
        for(Field f: fields){
            if(f.getAnnotation(com.project1.annotations.Id.class) != null){
                id.add(f.getAnnotation(com.project1.annotations.Id.class).name());
                columns.add(f.getAnnotation(com.project1.annotations.Id.class).name());
            }
            if(f.getAnnotation(com.project1.annotations.Column.class) != null) {
                columns.add(f.getAnnotation(Column.class).name());
            }
        }

        String sql = "select * from "+tableName+" where "+id.poll()+"="+primaryKey+";";
        try{
            Statement st = ConnectionUtil.getInstance().getConnection().createStatement();
            ResultSet resultSet = st.executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
//                    if (i > 1) System.out.print(",  ");
//                    String columnValue = resultSet.getString(i);
                    values.add(resultSet.getString(i));
//                    System.out.print(columnValue + " " + rsmd.getColumnName(i));// gets column names too
//                    System.out.print(columnValue); // saves the output as a string but i need it in a collection
//                    System.out.println(values); // this shows that i've gotten the data for the id
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        int size = columns.size();
        for(int i=0;i<size;i++){
            if(values.size() == 1){
                if(isNumeric(values.peek())) {
                    jsonBuilder.append("\"" + columns.poll() + "\":"+values.poll()+"}");
                    continue;
                }else{
                    jsonBuilder.append("\"" + columns.poll() + "\":\""+values.poll()+"\"}");
                    continue;
                }
            }
            if(isNumeric(values.peek())) {
                jsonBuilder.append("\"" + columns.poll() + "\":"+values.poll()+",");
                continue;
            }else{
                jsonBuilder.append("\"" + columns.poll() + "\":\""+values.poll()+"\",");
                continue;
            }
        }
        System.out.println(jsonBuilder);

        String json = jsonBuilder.toString();
        Gson gson = new Gson();
        Class clazz = c;
        Object o = gson.fromJson(json,c);

        return clazz.cast(o);
    } // this is being passed the class


    // to check if a value is a number to edit my own json string builder
    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public void updateTable(Object o){//the select by id needs to be used first to get a primary key
        //TODO: implement updateTable
        String tableName = o.getClass().getSimpleName().toLowerCase();
        Queue<String> columns = new LinkedList<>();
        Queue<String> columnsData = new LinkedList<>();

        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();

        //this get my annotations from the class of Object o passed and the annotations .name() gives the column name and .dataType() gives the sql data type acceptable for user
        for(Field f: fields){
            if(f.getAnnotation(com.project1.annotations.Id.class) != null){
                columns.add(f.getAnnotation(com.project1.annotations.Id.class).name());
                columnsData.add(f.getAnnotation(com.project1.annotations.Id.class).dataType());
            }
            if(f.getAnnotation(com.project1.annotations.Column.class) != null) {
                columns.add(f.getAnnotation(Column.class).name());
                columnsData.add(f.getAnnotation(Column.class).dataType());
            }
        }
    }


}

