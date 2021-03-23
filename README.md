# ORM project1

##The methods to achieve basic crud include:
Methods | Description
------------ | -------------
persist(Object o) | pass in the class object to create a table named after the class
insertInTable(Object o) | takes the object values and puts them in class table
selectAllSql(Object o) |returns all values in the class table name
cpp.findByIdObjectInput(Object o, int primary key)|returns object with class table values of primary key. The primary key must be a number for this operation to work.
cpp.dropTable(Object o)| will drop the class table from your database
cpp.findById(Object o, int primaryKey) or cpp.findById(Class c, int primaryKey)| Both of these methods will return the row of data in an object from given either an object, or class name. The convention for inputting classname is className.class. If you want to save these objects you have to cast them to the type of class, because they are returned as type Object.
updateTable(Object o) | This method should be used with findbyId() if the Table object isn't created yet to be passed to updateTable(). If you use findById you will have to type cast it to the class you are updating.


##The annotations to build your table include the following:
Annotations | Usage
------------ | -------------
@Table(name = )|This annotation should be put over the start of a class and be name after the class. So if the name of the class was Car you would put **name="Car"**.
@Id(name = , dataType = )|This annotation should be put over your primary key, you must specify the sql data type and add primary key. For example the name would be the field you are referencing like userId: **name="userId", dataType="Integer _primary key_".** The data type needs to be in reference to the sql language of your choice with the trailing **_primary key_** otherwise your talbe will not have a primary value. 
@Columns(name = , dataType = )|For every other field you wish to put into the class table you annotate with columns. Once again you must put the sql data type in after the field name. **name="age", dataType="Integer"**. If you wanted to create a foreign key **name="age", dataType="Integer _reference tableName_"** would be the correct syntax.

CustomPersistenceProvider is the class to perform sql alterations.