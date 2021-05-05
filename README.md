# ORM project1
# PROJECT NAME

## Project Description

A custom object relational mapping (ORM) framework, written in Java, which allows for a simplified and SQL-free interaction with a relational data source. Low-level JDBC is completely abstracted away from the developer, allowing them to easily query and persist data to a data source. Makes heavy use of the Java Reflection API in order to support any entity objects as defined by the developer. Additionally, the framework offers connection pooling to support multi-threaded applications.

## Technologies Used

* Java 
* SQL 
* JDBC
* JUnit
* PostgreSQL 
* Maven 
* AWS RDS
* Mockito 
* DevOps
* DBBeaver
* Git
* XML

## Features

List of features ready and TODOs for future development
* Provide developers a programmatic configuration of entities.
* Programmatic persistence of entities.
* Generated custom annotations for models.

To-do list:
* Transactional management.
* Connection pooling.

## Getting Started

   inside powershell or Unix
   git clone https://github.com/Jl3E/Project1.git
   edit database creditials


## Usage

##The methods to achieve basic crud include:
Methods | Description
------------ | -------------
create(Object o) | pass in the class object to create a table named after the class.
insertInTable(Object o) | takes the object values and puts them in class table.
selectByInnerJoin(Object o1, Object o2)| takes the object of a table with the object of the reference table. For example, if Employee primary key was a foreign key to Bank you would input(employee,bank) for the constructor.
selectAllSql(Object o) |returns all values in the class table name.
deleteById(Object o, int primaryKey)| deletes the row of data by int primary key.
dropTable(Object o)| will drop the class table from your database
findById(Object o, int primaryKey) or cpp.findById(o.getClass(), int primaryKey)| You can input an object or class with a primary key to create an object from the selected data. You should assign this return to a new object of your table class. Instead of **_o.getClass()_** you could do **_ClassName.class_** as well.
updateTable(Object o) | This method should be used with findById() if the Table object isn't created yet to be passed to **_updateTable()_**.
##The annotations to build your table include the following:
Annotations | Usage
------------ | -------------
@Table(name = )|This annotation should be put over the start of a class and be name after the class. So if the name of the class was Car you would put **name="Car"**.
@Id(name = , dataType = )|This annotation should be put over your primary key, you must specify the sql data type and add primary key. For example the name would be the field you are referencing like userId: **name="userId", dataType="Integer _primary key_".** The data type needs to be in reference to the sql language of your choice with the trailing **_primary key_** otherwise your talbe will not have a primary value. 
@Columns(name = , dataType = )|For every other field you wish to put into the class table you annotate with columns. Once again you must put the sql data type in after the field name. **name="age", dataType="Integer"**. If you wanted to create a foreign key **name="age", dataType="Integer _reference tableName_"** would be the correct syntax.

CustomPersistenceProvider is the class to perform sql alterations.


