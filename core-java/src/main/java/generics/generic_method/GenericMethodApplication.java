package generics.generic_method;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

public class GenericMethodApplication {
    public static void main(String[] args) {
        List<Product> productsList = List.of(
                new Product(1, "PC"),
                new Product(2, "Laptop"),
                new Product(3, "Screen"));

        Utils.print(productsList);
        System.out.println("------------------------");

        List<Employee> employeeList = List.of(
                new Employee(1, "Employee 1"),
                new Employee(2, "Employee 2"),
                new Employee(3, "Employee 3"));

        Utils.print(employeeList);

        System.out.println("------------------------");

        List<Manager> managerList = List.of(
                new Manager(1, "Manager 1"),
                new Manager(2, "Manager 2"),
                new Manager(3, "Manager 3"));

        List<INotify> peopleList = new ArrayList<>();
        peopleList.addAll(employeeList);
        peopleList.addAll(managerList);

        Utils.notify(peopleList);

    }
}

class Utils{
    public static <E> void print(List<E> elements){
        elements.forEach(System.out::println);
    }

    public static <E extends INotify> void notify(List<E> elements){
        elements.forEach(INotify::sendNotification);
    }
}

@Data
@AllArgsConstructor
@ToString
class Product{
    private Integer id;
    private String name;
}

@Data
@AllArgsConstructor
@ToString
class Employee implements INotify{
    private Integer id;
    private String name;

    @Override
    public void sendNotification() {
        System.out.println("Notification sent to Employee");
    }
}

@Data
@AllArgsConstructor
@ToString
class Manager implements INotify{
    private Integer id;
    private String name;

    @Override
    public void sendNotification() {
        System.out.println("Notification sent to Manager");
    }
}

interface INotify{
    void sendNotification();
}
