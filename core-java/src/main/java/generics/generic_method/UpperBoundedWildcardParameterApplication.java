package generics.generic_method;

import java.util.List;

public class UpperBoundedWildcardParameterApplication {
    public static void main(String[] args) {
        System.out.println(SumCalculator.sum(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
        System.out.println(SumCalculator.sum(List.of(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)));
    }
}

class SumCalculator {
    public static double sum(List<? extends Number> numbers){
        return numbers.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }
}
