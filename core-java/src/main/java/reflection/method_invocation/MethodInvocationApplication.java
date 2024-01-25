package reflection.method_invocation;

import java.lang.reflect.Method;

public class MethodInvocationApplication {
    public static void main(String[] args) {
        try{
            Class klazz = Class.forName("reflection.method_invocation.GreetHelper");

            Object clazzObject = klazz.getDeclaredConstructor().newInstance();

            Method declaredMethod = klazz.getDeclaredMethod("greet", String.class);
            declaredMethod.setAccessible(true);
            String greeting = (String) declaredMethod.invoke(clazzObject, "Tamer");

            System.out.println(greeting);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class GreetHelper {
    private String greet(String name){
        if(name == null || name.isEmpty())
            return "Hello, Stranger";

        return String.format("Hello, %s", name);
    }
}
