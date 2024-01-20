package generics.generic_method;

import java.util.List;

public class LowerBoundedWildcardParameterApplication {
    public static void main(String[] args) {
        List<Integer> integerList = List.of(1, 2, 3);
        List<Number> numbersList = List.of(1, 2, 3);
        List<Object> objectList = List.of(1, 2, 3);
        List<Double> doubleList = List.of(1.0, 2.0, 3.0);


        System.out.println(LowerBoundedSumCalculator.sum(numbersList));
        System.out.println(LowerBoundedSumCalculator.sum(integerList));
        System.out.println(LowerBoundedSumCalculator.sum(objectList));

        // the following will not compile, as Double is not a Super class for Integer
        //System.out.println(LowerBoundedSumCalculator.sum(doubleList));
    }
}

class LowerBoundedSumCalculator{
    public static double sum(List<? super Integer> numbers){
        return numbers.stream().mapToInt(number -> (Integer) number ).sum();
    }
}
