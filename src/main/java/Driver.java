import com.project1.model.Car;
import com.project1.model.Employee;
import com.project1.orm.CustomPersistenceProvider;

public class Driver {

    public static void main(String[] args){
        Employee e = new Employee(1, "Joey Elmblad", (float) 50.30, "Pleb"); // float is type casted
        Car c = new Car(1,"ford", (float) 50.50, false); //double needs to be casted to a float, wak, try double persision or something  instead maybe
        CustomPersistenceProvider cpp = new CustomPersistenceProvider();

        cpp.dropTable(e);
        cpp.dropTable(c);

        cpp.persist(e);
        cpp.persist(c);

        cpp.insertInTable(e);//TODO: make a method to print the table columns out for the print statement
        cpp.insertInTable(c);
        Car c3 = new Car(2,"ford", (float) 50.50, false);

        cpp.insertInTable(c3);

        cpp.selectAllSql(e);//TODO: check these with multiple inserts
        System.out.println("\n");
        cpp.selectAllSql(c);
        System.out.println("\n");
        Employee e2 = (Employee) cpp.findById(e,1);// which is better input of class object or class name?
        Car c2 = (Car) cpp.findById(c, 1);
        System.out.println(c2.getName());
        c2.setName("notFord");

        cpp.updateTable(c2);

        cpp.selectAllSql(c2);


//        cpp.persist(e);
//        cpp.insertInTable(e);
//        Car c2 = (Car) cpp.findByIdTest(Car.class,1); // type casted
//        c2.setName("notFord");
//        cpp.updateTable(c2);
//        cpp.selectAllSql(c2);//to check if ford changes to notFord







        //cpp.createTableSQL(e);

//----------------------------------testing-------------------------------------------------------------------------------
//        Car c2 = new Car(1,"ford", (float) 50.50, false);
//        cpp.persist(c2);  //create table
//        cpp.insertInTable(c2); //insert into said table
//        cpp.selectAllSql(c2); //read from said table
//        cpp.dropTable(c2);    //drop said table

//
//        if(cpp.doesTableExist(c2))
//            System.out.println("Exists");
//        else
//            System.out.println("Doesn't Exist");
//----------------------------working for createTable(persist), insertTable, select table, and drop table---------------------
//        Car c = new Car();   // this is where you place annotations to build your table
//        cpp.persist(c2);    // this creates the table of the object provided
//        cpp.selectAllSql(c2); //this is to get all the data from the table given object


        //Employee.class.getDeclaredFields() --> its own line
        //
//        for(Class<?> c: table){
//            tableName = c.toString();
//            tables.add(tableName);
            //System.out.println(tableName);
            // System.out.println(tN2);
//        }

//        Set<Class<?>> someClass = r.getTypesAnnotatedWith(Table.class);
//
//        for(Class c : someClass){
//            for(Field f: c.getDeclaredFields()){
//                Column column = f.getAnnotation(Column.class);
//                if(column !=null)
//                    System.out.println(column.name());
//            }
//        }

//        Employee employee1 = new Employee(2,"bob", (float) 69.6943,"Pleb 2 Pleb");
//
//        EmployeeService es = new EmployeeService();
//        es.createEmployee(employee1);
//
//        serializeEmloyee(); // this shows how to get a json from an POJO
//        deserializedEmployee(); // this shows how to input POJO attributes with a json
//
//        how could i use theses for an ORM? To create a table i could use Stringbuilder to split on, what about data type tho.
//        so i could save this json into sql somehow? ORM's dont use JDBC's?
    }

//    private static void serializeEmloyee(){
//        Employee employee = new Employee(3,"sam", (float) 50.321,"Alpha");
//
//        Gson gson = new Gson();
//        String json = gson.toJson(employee);
//        System.out.println(json);
//    }
//
//    private static void deserializedEmployee(){
//        String employeeJson = "{\"name\":\"sam\",\"id\":3,\"salary\":50.321,\"designation\":\"Alpha\"}";
//
//        Gson gson = new Gson();
//        Employee employee1 = gson.fromJson(employeeJson, Employee.class);
//
//    }
}
