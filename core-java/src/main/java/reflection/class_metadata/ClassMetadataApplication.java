package reflection.class_metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ClassMetadataApplication {
    public static void main(String[] args) {
        try{
            // Getting class name
            Class<?> c1 = Class.forName("reflection.class_metadata.ChildClass");
            System.out.println("-- Getting class name --");
            System.out.println(c1.getName());
            System.out.println(c1.getSimpleName());
            System.out.println();

            // Getting super class
            System.out.println("-- Getting super class --");
            System.out.println(c1.getSuperclass());
            System.out.println();

            // Getting implemented interfaces
            System.out.println("-- Getting implemented interfaces --");
            System.out.println(Arrays.toString(c1.getInterfaces()));
            System.out.println();

            // Getting class modifier
            Class<?> c2 = Class.forName("reflection.class_metadata.ClassMetadataApplication");
            System.out.println("-- Getting class modifier --");
            System.out.println(Modifier.toString(c2.getModifiers()));
            System.out.println();

            // Getting Fields (returning only public fields)
            // from both Class and its Parent as well
            System.out.println("-- Getting Fields --");
            System.out.println(Arrays.toString(c1.getFields()));
            System.out.println();

            // Getting declared Fields (returning both private and public fields)
            System.out.println("-- Getting declared Fields --");
            System.out.println(Arrays.toString(c1.getDeclaredFields()));
            System.out.println();

            // Getting Constructors
            System.out.println("-- Getting Constructors --");
            System.out.println(Arrays.toString(c1.getConstructors()));
            System.out.println();

            // Getting declared Constructors
            System.out.println("-- Getting declared Constructors --");
            System.out.println(Arrays.toString(c1.getDeclaredConstructors()));
            System.out.println();

            // Getting Methods
            System.out.println("-- Getting Methods --");
            System.out.println(Arrays.toString(c1.getMethods()));
            System.out.println();

            // Getting declared Methods
            System.out.println("-- Getting declared Methods --");
            System.out.println(Arrays.toString(c1.getDeclaredMethods()));
            System.out.println();

            // Getting Annotations
            System.out.println("-- Getting Annotations --");
            System.out.println(Arrays.toString(c1.getAnnotations()));
            System.out.println();

            // Getting declared Annotations
            System.out.println("-- Getting declared Annotations --");
            System.out.println(Arrays.toString(c1.getDeclaredAnnotations()));
            System.out.println();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

@Getter
@AllArgsConstructor
class Parent {
    private String name;

    public void displayName(){
        System.out.println(name);
    }
}

interface IntTest {
    void showValue();
}


class ChildClass extends Parent implements IntTest {
    private int value;
    public String publicValue;

    public ChildClass(String name, int value) {
        super(name);
        this.value = value;
    }

    @Override
    public void showValue() {
        System.out.println("value = " + value);
    }
}