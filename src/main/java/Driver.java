import com.project1.model.Bank;
import com.project1.model.Car;
import com.project1.model.Employee;
import com.project1.orm.CustomPersistenceProvider;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public class Driver {//TODO: FIX README

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, IOException, SAXException, ParserConfigurationException, ExecutionException, InterruptedException {
        CustomPersistenceProvider cpp = new CustomPersistenceProvider();
        Employee e = new Employee(1, "Joey ", 100000, "Team A");
        Bank b = new Bank(13432543,1,"Chase",67.45);


//--------------------Testing cache------------------------------------------------
//        cpp.setCacheOn();
//        cpp.create(b);
//        cpp.insertInTable(b);
//        cpp.selectAllSql(b);
//        cpp.selectByInnerJoin(e,b);
//        cpp.selectAllSql(b);
//        System.out.println("Create this object from the data base");
//        Bank bank = cpp.findById(Bank.class,13432543);
//        bank.setBankName("not chase");
//        cpp.updateTable(bank);
//        cpp.selectAllSql(b);
//        System.out.println("Delete this object from data base");
//        cpp.deleteById(b, 13432543);
//        cpp.selectAllSql(b);
//        cpp.dropTable(b);

//------------------------create employee table and insert values-------------------
//        cpp.dropTable(e);
//        cpp.create(e);
//        cpp.insertInTable(e);
//        cpp.selectAllSql(e);
//--------------------------create bank table and insert values----------------------
//        cpp.dropTable(b);
//        cpp.create(b);
//        cpp.insertInTable(b);
//        cpp.selectAllSql(b);
//--------------------------inner join employee and bank objects--------------------
//        cpp.selectByInnerJoin(e,b);
//--------------------------delete value by id---------------------------------------
//        Employee e2 = new Employee(2, "Sam", 75000, "Team B");
//        cpp.insertInTable(e2);
//        cpp.selectAllSql(e2);
//        System.out.println("Now to delete "+e2.getName());
//        cpp.deleteById(e2,2);
//        cpp.selectAllSql(e2);
//------------------------update value in table---------------------------------------
//        Employee employee = cpp.findById(e, 1);
//        employee.setName("Not Joey");
//        cpp.updateTable(employee);
//        cpp.selectAllSql(employee);
    }
}
