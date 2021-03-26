package com.project1.orm;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project1.annotations.*;
import com.project1.config.ApplicationUtil;
import com.project1.config.DataSource;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class CustomPersistenceProvider {
    private Deque<String> statements = new ArrayDeque<>();
    private Queue<String> valuesId;
    private Queue<String> columnsId;
    private Boolean cacheOn = false;

    public CustomPersistenceProvider() { }

    public void setCacheOn(){
        cacheOn = true;
    }

    public void setCacheOff(){
        cacheOn = false;
    }

    public void persist(){
        if(!cacheOn){
            System.out.println("Set your cache on with the command: cpp.setCacheOn();");
        }else{
            int size = statements.size();
            for(int j=0; j< size;j++) {
                if(statements.peek().contains("update")){
                    try {
                        Statement st = DataSource.getConnection().createStatement();
                        int i = st.executeUpdate(statements.poll());
                        System.out.println("The number of updated rows were " + i);
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if(statements.peek().contains("where")){
                    try {
                        Queue<String> values = new LinkedList<>();
                        Statement st = DataSource.getConnection().createStatement();
                        ResultSet resultSet = st.executeQuery(statements.poll());
                        ResultSetMetaData rsmd = resultSet.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        while (resultSet.next()) {
                            for (int i = 1; i <= columnsNumber; i++) {
                                values.add(resultSet.getString(i));
                            }
                        }
                        valuesId=values;
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if(statements.peek().contains("join")){
                    try {
                        Statement st = DataSource.getConnection().createStatement();
                        ResultSet resultSet = st.executeQuery(statements.poll());
                        ResultSetMetaData rsmd = resultSet.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        while (resultSet.next()) {
                            for (int i = 1; i <= columnsNumber; i++) {
                                if (i > 1) System.out.print(",  ");
                                String columnValue = resultSet.getString(i);
                                System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                            }
                            System.out.println("");
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }

                }else if(statements.peek().contains("select")){

                    try {
                        Statement st = DataSource.getConnection().createStatement();
                        ResultSet resultSet = st.executeQuery(statements.poll());
                        ResultSetMetaData rsmd = resultSet.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
//                        tableTitle(o);// wont' have titles? dang
                        while (resultSet.next()) {
                            for (int i = 1; i <= columnsNumber; i++) {
                                if (i > 1) System.out.print(",  ");
                                String columnValue = resultSet.getString(i);
                                System.out.print(columnValue);
                            }
                            System.out.println("");
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    continue;
                } else{
                    try {
                        Statement st = DataSource.getConnection().createStatement();
                        int i = st.executeUpdate(statements.poll());
                        System.out.println("The number of updated rows were " + i);
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                }
        }
    }

    public Queue<String> getSql(){

        return statements;
    }

    public void setSql(String sql){
        statements.add(sql);
    }

    public boolean doesTableExist(Object o) {//TODO FIX THIS, NOT WORKING NOW, fixed! needed to have the table saved to lowercase.
        String tableNameCheck = o.getClass().getSimpleName().toLowerCase();//the name of the tables will be the model class names.

        try {
            Connection con = DataSource.getConnection();

            DatabaseMetaData meta = con.getMetaData();
            ResultSet res = meta.getTables(null, null, null,
                    new String[]{"TABLE"});
            while (res.next()) {
                if (tableNameCheck.equals(res.getString("TABLE_NAME"))) {
//                    System.out.println("Object table already exists.");// BreadCrumb to test if boolean switches
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

    public void create(Object o) {
        Queue<String> columns = new LinkedList<>();//this holds the table names and data types

        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String className = o.getClass().getSimpleName(); // this gives the classname to be used as the table name

        //this get my annotations from the class of Object o passed and the annotations .name() gives the column name and .dataType() gives the sql data type acceptable for user
        for (Field f : fields) {
            if (f.getAnnotation(com.project1.annotations.Id.class) != null) {
                columns.add(f.getAnnotation(com.project1.annotations.Id.class).name());
                columns.add(f.getAnnotation(com.project1.annotations.Id.class).dataType());
            }
            if (f.getAnnotation(com.project1.annotations.Column.class) != null) {
                columns.add(f.getAnnotation(Column.class).name());
                columns.add(f.getAnnotation(Column.class).dataType());
            }
        }

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("create table ");
        sqlBuilder.append(className.toLowerCase() + "(");
        int size = columns.size() / 2;

        for (int i = 0; i < size; i++) {
            if (columns.size() == 2) { // this is hard coded as 2 because the last two elements in the queue will be the column name and data type
                sqlBuilder.append(columns.poll() + " " + columns.poll() + ");");
                break;
            }
            sqlBuilder.append(columns.poll() + " " + columns.poll() + ", ");
        }
        //System.out.println(sqlBuilder); // to test the database with first

        String sql = String.valueOf(sqlBuilder);
        setSql(sql);
        if(!cacheOn) {
            if (!doesTableExist(o)) {
                try {
                    Statement st = DataSource.getConnection().createStatement();
                    int i = st.executeUpdate(sql);
                    System.out.println("The number of updated rows were " + i);
                    statements.removeLast();// if I run this out of thread its not collected
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }else{
            persist();
        }
    }

    public void insertInTable(Object o) {
        String className = o.getClass().getSimpleName(); // this gives the classname to be used as the table name

        Gson gson = new Gson();
        String json = gson.toJson(o);
        String yourJson = json;
        JsonElement element = new JsonParser().parse(json);
        JsonObject obj = element.getAsJsonObject(); //since you know it's a JsonObject
        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();//will return members of your object

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("insert into ");
        sqlBuilder.append(className.toLowerCase() + " values (");
        int size = entries.size();
        int indexValues = 0;
        for (Map.Entry<String, JsonElement> entry : entries) {
            String check = String.valueOf(entry.getValue());
            if (check.contains("\"")) {
                String newCheck = check.replace("\"", "\'");
                if (indexValues == entries.size() - 1) { // this is hard coded as 1 because the last value should close the statement
                    sqlBuilder.append(newCheck + ");");
                    break;
                }
                sqlBuilder.append(newCheck + ", ");
                indexValues++;
                continue;  // this is need to skip over the other ford with quotes being added to the StringBuilder
            }
            if (indexValues == entries.size() - 1) { // this is hard coded as 1 because the last value should close the statement
                sqlBuilder.append(entry.getValue() + ");");
                break;
            }
            sqlBuilder.append(entry.getValue() + ", ");
            indexValues++;
        }

        String sql = String.valueOf(sqlBuilder);
        setSql(sql); //this is collecting the
        if(!cacheOn) {
            try {
                Statement st = DataSource.getConnection().createStatement();
                int i = st.executeUpdate(sql);
                System.out.println("The number of updated rows were " + i);
                statements.removeLast();// if I run this out of thread its not collected
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }else{
            persist();
        }

    }

    public void selectByInnerJoin(Object o1, Object o2){// takes the object of a table with the object of the reference table.
        String tableNameO1 = o1.getClass().getSimpleName().toLowerCase();
        Queue<String> id = new LinkedList<>();
        String tableNameO2 = o2.getClass().getSimpleName().toLowerCase();
        Queue<String> rKey = new LinkedList<>();
        Field[] fieldsO1 = o1.getClass().getDeclaredFields();
        Field[] fieldsO2 = o2.getClass().getDeclaredFields();

        for(Field f: fieldsO1){
            if (f.getAnnotation(com.project1.annotations.Id.class) != null) {
                id.add(f.getAnnotation(com.project1.annotations.Id.class).name());
            }
        }

        for(Field f: fieldsO2){
            if (f.getAnnotation(com.project1.annotations.Column.class) != null && f.getAnnotation(com.project1.annotations.Column.class).toString().contains("reference")) {
                rKey.add(f.getAnnotation(com.project1.annotations.Column.class).name());
            }
        }
        System.out.println("");
        String sql = "select * from "+tableNameO1+" inner join "+tableNameO2+" on "+tableNameO1+"."+id.poll() +"="+ tableNameO2+"."+rKey.poll()+";";
        setSql(sql);
        if(!cacheOn) {
            try {
                Statement st = DataSource.getConnection().createStatement();
                ResultSet resultSet = st.executeQuery(sql);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = resultSet.getString(i);
                        System.out.print(rsmd.getColumnName(i) + ": " + columnValue);// gets column names too
//                    System.out.print(columnValue);
                    }
                    System.out.println("");
                }
                statements.removeLast();// if I run this out of thread its not collected
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }else{
            persist();
        }

    }

    public void selectAllSql(Object o) {
        String className = o.getClass().getSimpleName().toLowerCase();
        String sql = "select * from " + className + ";";
        setSql(sql);
        System.out.println("");
        if(!cacheOn) {
            if (doesTableExist(o)) {
                try {
                    Statement st = DataSource.getConnection().createStatement();
                    ResultSet resultSet = st.executeQuery(sql);
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    tableTitle(o);
                    while (resultSet.next()) {
                        for (int i = 1; i <= columnsNumber; i++) {
                            if (i > 1) System.out.print(",  ");
                            String columnValue = resultSet.getString(i);
//                    System.out.print(columnValue + " " + rsmd.getColumnName(i));// gets column names too
                            System.out.print(columnValue);
                        }
                        System.out.println("");
                    }
                    statements.removeLast();// if I run this out of thread its not collected
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                System.out.println("Your table doesn't exist bud.");
                create(o);
                System.out.println("Now it does!");
            }
        }else{
            persist();
        }

    }

    public void dropTable(Object o) {
        String tableName = o.getClass().getSimpleName().toLowerCase();
        String sql = "drop table " + tableName +" CASCADE;";
        setSql(sql);
        if(doesTableExist(o)) {
            if(!cacheOn) {
                try {
                    Statement st = DataSource.getConnection().createStatement();
                    int i = st.executeUpdate(sql);
                    System.out.println("The number of updated rows were " + i);
                    statements.removeLast();// if I run this out of thread its not collected
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }else{
                persist();
            }
        }
    }

    // can't cache this
    public <T>T findById(Object o, int primaryKey) throws ClassNotFoundException {
        String tableName = o.getClass().getSimpleName().toLowerCase();
        Queue<String> columns = new LinkedList<>();
        Queue<String> id = new LinkedList<>();
        Queue<String> values = new LinkedList<>();
//        Class clazz = o.getClass();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.getAnnotation(com.project1.annotations.Id.class) != null) {
                id.add(f.getAnnotation(com.project1.annotations.Id.class).name());
                columns.add(f.getAnnotation(com.project1.annotations.Id.class).name());
            }
            if (f.getAnnotation(com.project1.annotations.Column.class) != null) {
                columns.add(f.getAnnotation(Column.class).name());
            }
        }
        String sql = "select * from " + tableName + " where " + id.poll() + "=" + primaryKey + ";";
        setSql(sql);
        if(!cacheOn) {
            try {
                Statement st = DataSource.getConnection().createStatement();
                ResultSet resultSet = st.executeQuery(sql);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (resultSet.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        values.add(resultSet.getString(i));
                    }
                }
                statements.removeLast();//If this try statement goes off don't populate the statements
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }else{
            persist();
            String json = jsonBuilder(columns, valuesId);
            Gson gson = new Gson();
            Object obj = gson.fromJson(json, o.getClass());

            return (T) obj;
        }
        String json = jsonBuilder(columns, values);
        Gson gson = new Gson();
        Object obj = gson.fromJson(json, o.getClass());

        return (T) obj;
    }

    // can't cache this
    public <T>T findById(Class<T> c, int primaryKey) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String tableName = c.getSimpleName().toLowerCase();
//        System.out.println(tableName); //crumbs
        Queue<String> columns = new LinkedList<>();
        Queue<String> id = new LinkedList<>();
        Queue<String> values = new LinkedList<>();
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            if (f.getAnnotation(com.project1.annotations.Id.class) != null) {
                id.add(f.getAnnotation(com.project1.annotations.Id.class).name());
                columns.add(f.getAnnotation(com.project1.annotations.Id.class).name());
            }
            if (f.getAnnotation(com.project1.annotations.Column.class) != null) {
                columns.add(f.getAnnotation(Column.class).name());
            }
        }

        String sql = "select * from " + tableName + " where " + id.poll() + "=" + primaryKey + ";";
        setSql(sql);
        if(!cacheOn) {
            try {
                Statement st = DataSource.getConnection().createStatement();
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
                statements.removeLast();//If this try statement goes off don't populate the statements
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }else{
            persist();
            String json = jsonBuilder(columns, valuesId);
            Gson gson = new Gson();
            Class clazz = c;
            Object o = gson.fromJson(json, c);

            return (T) o;
        }

        String json = jsonBuilder(columns, values);
        System.out.println(json);
        Gson gson = new Gson();
        Class clazz = c;
        Object o = gson.fromJson(json, c);

//       return c.cast(o);
        return (T) o;
    } // this is being passed the class

    public String jsonBuilder(Queue<String> columns, Queue<String> values ){
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            if (values.size() == 1) {
                if (isNumeric(values.peek())) {
                    jsonBuilder.append("\"" + columns.poll() + "\":" + values.poll() + "}");
                    continue;
                } else {
                    jsonBuilder.append("\"" + columns.poll() + "\":\"" + values.poll() + "\"}");
                    continue;
                }
            }
            if (isNumeric(values.peek())) {
                jsonBuilder.append("\"" + columns.poll() + "\":" + values.poll() + ",");
                continue;
            } else {
                jsonBuilder.append("\"" + columns.poll() + "\":\"" + values.poll() + "\",");
                continue;
            }
        }
//        System.out.println(jsonBuilder);//crumbs
        String json = jsonBuilder.toString();

        return json;
    }

    // to check if a value is a number to edit my own json string builder
    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public void updateTable(Object o) {//the select by id needs to be used first to get a primary key
        String tableName = o.getClass().getSimpleName().toLowerCase();

        Gson gson = new Gson();
        String json = gson.toJson(o);
        JsonElement element = new JsonParser().parse(json);
        JsonObject obj = element.getAsJsonObject(); //since you know it's a JsonObject
        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();//will return members of your object

        StringBuilder sqlBuilder = new StringBuilder();
        StringBuilder sqlBuilder2 = new StringBuilder();
        sqlBuilder.append("update ");
        sqlBuilder.append(tableName.toLowerCase()+" set ");
        int size = entries.size();
        int index = 0;
        for (Map.Entry<String, JsonElement> entry : entries) {
            String check = String.valueOf(entry.getValue());
            if (check.contains("\"")) {
//                System.out.println("yea it contains \" what about it? HUH?!"); // needed to replace (")'s w/ (')'s
                String newCheck = check.replace("\"", "\'");
                if(index == 0){
                    sqlBuilder2.append("where "+entry.getKey()+"="+newCheck+";");
                    index++;
                    continue;
                }else if(index==1){
                    sqlBuilder.append(entry.getKey() + "="+newCheck+" ");
                    index++;
                    continue;
                }
                sqlBuilder.append(", "+entry.getKey() + "="+newCheck+" ");
                continue;  // this is need to skip over the other ford with quotes being added to the StringBuilder
            }
            if(index == 0){
                sqlBuilder2.append("where "+entry.getKey()+"="+entry.getValue()+";");
                index++;
                continue;
            }else if(index==1){
                sqlBuilder.append(entry.getKey() + "="+entry.getValue()+" ");
                index++;
                continue;
            }
            sqlBuilder.append(", "+entry.getKey()+"="+entry.getValue()+" ");

        }
//        System.out.println(sqlBuilder.toString()+sqlBuilder2.toString());// crumbs to check
        String sql = sqlBuilder.toString()+sqlBuilder2.toString();
        setSql(sql);
        if(!cacheOn) {
            try {
                Statement st = DataSource.getConnection().createStatement();
                int i = st.executeUpdate(sql);
                System.out.println("The number of updated rows were " + i);
                statements.removeLast();//If this try statement goes off don't populate the statements
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }else{
            persist();
        }
    }

    public void deleteById(Object o, int primaryKey) {
        String tableName = o.getClass().getSimpleName().toLowerCase();
        Queue<String> id = new LinkedList<>();
        Field[] fields = o.getClass().getDeclaredFields();

        for (Field f : fields) {
            if (f.getAnnotation(com.project1.annotations.Id.class) != null)
                id.add(f.getAnnotation(com.project1.annotations.Id.class).name().toString());
        }

        String sql = "delete from " + tableName + " where " + id.poll() + "=" + primaryKey + ";";
        setSql(sql);
        if(!cacheOn) {
            try {
                Statement st = DataSource.getConnection().createStatement();
                int i = st.executeUpdate(sql);
                System.out.println("The number of updated rows were " + i);
                statements.removeLast();//If this try statement goes off don't populate the statements
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }else{
            persist();
        }
    }

    public void tableTitle(Object o){
        Queue<String> columns = new LinkedList<>();//this holds the table names and data types

        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();

        //this get my annotations from the class of Object o passed and the annotations .name() gives the column name and .dataType() gives the sql data type acceptable for user
        for (Field f : fields) {
            if (f.getAnnotation(com.project1.annotations.Id.class) != null) {
                columns.add(f.getAnnotation(com.project1.annotations.Id.class).name());
            }
            if (f.getAnnotation(com.project1.annotations.Column.class) != null) {
                columns.add(f.getAnnotation(Column.class).name());
            }
        }
        while(columns.peek() != null){
            System.out.print(columns.poll()+"  ");
            if(columns.peek() == null)
                System.out.println("");
        }

    }
}

