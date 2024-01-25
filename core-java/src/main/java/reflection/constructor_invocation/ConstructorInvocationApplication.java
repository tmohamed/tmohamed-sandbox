package reflection.constructor_invocation;

import lombok.*;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ConstructorInvocationApplication {
    public static void main(String[] args) {
        try{
            Class clazz = Class.forName("reflection.constructor_invocation.Model");

            System.out.println("-- Getting Constructor with 2 args --");
            Constructor twoArgsConstructor = clazz.getConstructor(String.class, int.class);
            System.out.println(twoArgsConstructor);
            Model twoArgsObject = (Model) twoArgsConstructor.newInstance(new Object[] {"Tamer Mohamed", 36});
            twoArgsObject.print();
            System.out.println();


            System.out.println("-- Getting all public Constructors of the class --");
            Constructor[] publicConstructors = clazz.getConstructors();
            System.out.println(Arrays.toString(publicConstructors));
            System.out.println();

            System.out.println("-- Getting private Constructor of the class --");
            Constructor privateConstructor = clazz.getDeclaredConstructor();
            System.out.println(privateConstructor);

            privateConstructor.setAccessible(true);
            Model privateObject = (Model) privateConstructor.newInstance();
            privateObject.print();
            System.out.println();

            System.out.println("-- Getting all declared Constructors of the class --");
            Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
            System.out.println(Arrays.toString(declaredConstructors));
            System.out.println();

            System.out.println("-- Getting Not-Found Constructor with 2 args --");
            Constructor notFoundConstructor = clazz.getConstructor(Integer.class, String.class);
            System.out.println(notFoundConstructor);
            System.out.println();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
class Model {
    private String key;
    private int value;

    public void print(){
        System.out.println(String.format("Key: %s, Value: %s", key, value));
    }
}