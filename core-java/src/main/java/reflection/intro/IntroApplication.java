package reflection.intro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class IntroApplication {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> klass = Class.forName("reflection.intro.Person");

        System.out.println("-- Getting Constructors --");
        Constructor<?>[] constructors = klass.getConstructors();
        System.out.println("Constructors: " + Arrays.toString(constructors));
        System.out.println();

        System.out.println("-- Getting All Methods, even inherited ones --");
        Method[] allMethods = klass.getMethods();
        System.out.println("All Methods: " + Arrays.toString(allMethods));
        System.out.println();


        System.out.println("-- Getting Class Methods --");
        Method[] classMethods = klass.getDeclaredMethods();
        System.out.println("Class Methods: " + Arrays.toString(classMethods));
        System.out.println();


        System.out.println("-- Getting All Fields, even inherited ones --");
        Field[] allFields = klass.getFields();
        System.out.println("All Fields: " + Arrays.toString(allFields));
        System.out.println();

        System.out.println("-- Getting Class Fields --");
        Field[] classFields = klass.getDeclaredFields();
        System.out.println("All Fields: " + Arrays.toString(classFields));
        System.out.println();

    }
}

@Getter
@Setter
@AllArgsConstructor
class Person {
    private String name;
    private Integer age;
}
