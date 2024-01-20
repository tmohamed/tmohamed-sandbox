package generics.generic_method;

import java.util.List;

public class UpperBoundedParameterApplication {
    public static void main(String[] args) {
        AverageCalculator<Integer> averageCalculator = new AverageCalculator<>();
        averageCalculator.setNumbers(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        System.out.println(averageCalculator.calculateAverage());
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
