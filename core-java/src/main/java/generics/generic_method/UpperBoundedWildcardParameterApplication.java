package generics.generic_method;

import java.util.List;

public class UpperBoundedWildcardParameterApplication {
    public static void main(String[] args) {
        System.out.println(SumCalculator.sum(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
        System.out.println(SumCalculator.sum(List.of(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)));

        SumCalculatorV2<Integer> sumCalculatorV2 = new SumCalculatorV2<>();
        System.out.println(sumCalculatorV2.sum(List.of(1, 2, 3, 4, 5)));
        System.out.println(sumCalculatorV2.sumV2(List.of(1, 2, 3, 4, 5)));

        // the following will not compile, as
        // while Integer & Double are subtypes of Number
        // List<Integer> & List<Double> are NOT subtypes of List<Number>
        // That's why wildcards are needed
        //System.out.println(sumCalculatorV2.sum(List.of(1.0, 2.0, 3.0, 4.0, 5.0)));

        List<Integer> integerList = List.of(1, 2, 3, 4, 5);
        List<Double> doubleList = List.of(1.0, 2.0, 3.0, 4.0, 5.0);

        // the following will not compile, as
        // while Integer & Double are subtypes of Number
        // List<Integer> & List<Double> are NOT subtypes of List<Number>
        // That's why wildcards are needed
        /* System.out.println(NotGenericSumCalculator.sum(integerList));
        System.out.println(NotGenericSumCalculator.sum(doubleList)); */

        System.out.println(Util.genericSumV2(integerList));
        System.out.println(Util.genericSumV2(doubleList));

    }
}

class NotGenericSumCalculator{
    public static double sum(List<Number> numbers){
        return Util.nonGenericSum(numbers);
    }
}

class SumCalculator {
    public static double sum(List<? extends Number> numbers){
        return Util.genericSum(numbers);
    }
}

class SumCalculatorV2<T extends Number> {
    public double sum(List<T> numbers){
        return Util.genericSum(numbers);
    }

    public double sumV2(List<T> numbers){
        return Util.genericSumV2(numbers);
    }
}

class Util {
    public static double nonGenericSum(List<Number> numbers){
        return numbers.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    public static <T extends Number> double genericSum(List<T> numbers){
        return numbers.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    public static double genericSumV2(List<? extends Number> numbers){
        return numbers.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }
}