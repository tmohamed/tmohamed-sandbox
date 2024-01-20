package generics.generic_method;

import java.util.List;

public class UpperBoundedParameterApplication {
    public static void main(String[] args) {
        AverageCalculator<Integer> averageCalculator = new AverageCalculator<>();
        averageCalculator.setNumbers(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        System.out.println(averageCalculator.calculateAverage());


        // this will not Compile as
        // both Integer and Double are subtypes of Number
        // while List<Integer> && List<Double> are NOT subtypes of List<Number>
        // that's why wildcards are needed in other example: UpperBoundedWildcardParameterApplication.java
        // averageCalculator.setNumbers(List.of(1.0, 2.0));

    }
}

class AverageCalculator<T extends Number> {
    private List<T> numbers;

    public void setNumbers(List<T> numbers) {
        this.numbers = numbers;
    }

    public double calculateAverage(){
        return this.numbers.stream()
                .mapToDouble(Number::doubleValue)
                .average().getAsDouble();
    }
}
