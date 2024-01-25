package reflection.getters_and_setters_invocation;

import java.lang.reflect.Field;

public class GettersAndSettersInvocationApplication {
    public static void main(String[] args) {
        try{
            Class clazz = Class.forName("reflection.getters_and_setters_invocation.Person");
            Field[] declaredFields = clazz.getDeclaredFields();
            StringBuffer stringBuffer = new StringBuffer();
            for(Field field : declaredFields){
                String fieldName = field.getName();
                String fieldType = field.getType().getSimpleName();
                CommonUtils.createSetter(fieldName, fieldType, stringBuffer);
                CommonUtils.createGetter(fieldName, fieldType, stringBuffer);
            }

            System.out.println(stringBuffer.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

class Person {
    private String name;
    private int age;
    private boolean vegetarian;
}

class CommonUtils {

    public static void createSetter(String fieldName, String fieldType, StringBuffer stringBuffer) {
        String setterMethod = String.format("public void set%s(%s %s){this.%s = %s;}", camelCase(fieldName), fieldType, fieldName, fieldName, fieldName);
        stringBuffer.append(setterMethod);
        stringBuffer.append("\n");

    }

    public static void createGetter(String fieldName, String fieldType, StringBuffer stringBuffer) {
        String getterMethod = String.format("public %s %s%s(){return this.%s;}",fieldType, fieldType.equals("boolean") ? "is" : "get" , camelCase(fieldName), fieldName);
        stringBuffer.append(getterMethod);
        stringBuffer.append("\n");
    }

    public static String camelCase(String message){
        return message.substring(0, 1).toUpperCase() + message.substring(1);
    }
}