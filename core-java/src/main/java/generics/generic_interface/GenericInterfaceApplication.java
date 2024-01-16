package generics.generic_interface;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

public class GenericInterfaceApplication {
    public static void main(String[] args) {
        JpaRepository<Employee, Long> employeeRepository = new JpaRepository();
        employeeRepository.save(new Employee(1L, "Tamer Mohamed"));
        employeeRepository.save(new Employee(2L, "Ahmed Saeed"));

        System.out.println();

        Employee employee1 = employeeRepository.findOneById(1L);
        Employee employee2 = employeeRepository.findOneById(2L);
    }
}

@Data
@AllArgsConstructor
@ToString
class Employee{
    private Long id;
    private String name;
}

interface Repository<T, ID>{
    T save (T object);
    T findOneById(ID id);
}

class JpaRepository<T, ID> implements Repository<T, ID>{

    @Override
    public T save(T entity) {
        // Save object to DB
        System.out.println(String.format("%s has been saved to DB", entity));
        return entity;
    }

    @Override
    public T findOneById(ID id) {
        System.out.println(String.format("ID: %s has been fetched from DB", id));
        // Find object by ID
        return null;
    }
}
