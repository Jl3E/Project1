import com.project1.model.Bank;
import com.project1.model.Car;
import com.project1.model.Employee;
import com.project1.orm.CustomPersistenceProvider;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public class Driver {

    public static void main(String[] args) throws Exception {
        CustomPersistenceProvider cpp = new CustomPersistenceProvider();
        Employee e = new Employee(1, "Joey ", 100000, "Team A");
        Employee e2 = new Employee(3, "Zack ", 50000, "Team B");
        Bank b = new Bank(13432543,1,"Chase",67.45);
        Bank b2 = new Bank(16512543,2,"Bank of America",67.45);


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
//        Employee e3 = new Employee(2, "Sam", 75000, "Team B");
//        cpp.insertInTable(e3);
//        cpp.selectAllSql(e3);
//-----
//        System.out.println("Now to delete "+e3.getName());
//        cpp.deleteById(e3,2);
//        cpp.selectAllSql(e3);
//------------------------update value in table---------------------------------------
//        Employee employee = cpp.findById(e, 1);
//        employee.setName("Not Joey");
//        cpp.updateTable(employee);
//        cpp.selectAllSql(employee);





//--------------------Testing cache------------------------------------------------
//        cpp.setCacheOn();
//        cpp.create(e);
//        cpp.insertInTable(e);
//        cpp.insertInTable(e2);
//        cpp.dropTable(b);
//        cpp.create(b);
//        cpp.insertInTable(b);
//        cpp.insertInTable(b2);
//        cpp.selectAllSql(b);
//--------------------------------------------------------------------
//        cpp.selectByInnerJoin(e,b);
//        System.out.println("Create this object from the data base");
//        Bank bank = cpp.findById(b,13432543);
//        bank.setBankName("not chase");
//        cpp.updateTable(bank);
//        cpp.selectAllSql(b);
//------------------------------------------------------------------
//        System.out.println("Delete this object from data base");
//        cpp.deleteById(b2, 16512543);
//        cpp.selectAllSql(b);
//
//        cpp.dropTable(e);
    }
}
