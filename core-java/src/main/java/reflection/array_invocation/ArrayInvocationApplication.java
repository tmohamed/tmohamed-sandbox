package reflection.array_invocation;

import lombok.*;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ArrayInvocationApplication {
    public static void main(String[] args) {
        try{
            Class clazz = Class.forName("reflection.array_invocation.Person");
            Field[] declaredFields = clazz.getDeclaredFields();
            for(Field field: declaredFields){
                Class fieldType = field.getType();
                if(fieldType.isArray()){
                    System.out.println(String.format("Array Found: %s", field.getName()));
                }
            }

            String[] phoneNumbers = (String[]) Array.newInstance(String.class, 2);
            Array.set(phoneNumbers, 0, "01xxxxxxxxx");
            Array.set(phoneNumbers, 1, "01xxxxxxxxy");

            Constructor declaredConstructor = clazz.getDeclaredConstructor(String.class, String[].class);
            Person person = (Person) declaredConstructor.newInstance(new Object[]{"Tamer Mohamed", phoneNumbers});
            System.out.println(person);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
class Person {
    private String name;
    private String[] phoneNumbers;
}