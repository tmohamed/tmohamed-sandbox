package reflection.variable_invocation;

import lombok.Getter;

import java.lang.reflect.Field;

public class VariableInvocationApplication {
    public static void main(String[] args) {
        try{
            Class clazz = Class.forName("reflection.variable_invocation.GreetHelper");
            Object clazzObject = clazz.getDeclaredConstructor().newInstance();
            Field declaredField = clazz.getDeclaredField("name");
            declaredField.setAccessible(true);
            declaredField.set(clazzObject, "Tamer Mohamed");

            System.out.println(declaredField.get(clazzObject));

            GreetHelper greetHelper = (GreetHelper) clazzObject;
            System.out.println(greetHelper.getName());


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

@Getter
class GreetHelper {
    private String name;
}