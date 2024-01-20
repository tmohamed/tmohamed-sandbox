package generics.generic_method;

import java.util.List;

public class UnBoundedWildcardParameterApplication {
    public static void main(String[] args) {
        List<Integer> integerList = List.of(1, 2, 3);
        List<String> stringList = List.of("Ahmed", "Mohamed", "Tamer");

        Printer.printToConsole(integerList);
        System.out.println("-------");
        Printer.printToConsole(stringList);

        System.out.println("-------");

        // both below will not compile
        // as both List<Integer> & List<String> are not subtypes of List<Object>
        // Printer.printObjectsToConsole(integerList);
        // System.out.println("-------");
        // Printer.printObjectsToConsole(stringList);

    }
}

class Printer{

    // Used when the functionality is provided by Object class
    public static void printToConsole(List<?> elements){
        elements.stream().forEach(System.out::println);
    }

    public static void printObjectsToConsole(List<Object> elements){
        elements.stream().forEach(System.out::println);
    }
}