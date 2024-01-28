package strategy_functional;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Predicate;

public class StrategyFunctionalApplication {
    public static void main(String[] args) {
        Predicate<Order> allOrdersFilter = (order) -> true;
        Predicate<Order> physicalOrdersFilter = (order) -> order.getType().equals(OrderType.PHYSICAL);
        Predicate<Order> electronicOrderFilter = (order) -> order.getType().equals(OrderType.ELECTRONIC);

        List<Order> orderList = List.of(
                new Order(OrderType.PHYSICAL, 1000.0),
                new Order(OrderType.PHYSICAL, 2000.0),
                new Order(OrderType.ELECTRONIC, 1200.0),
                new Order(OrderType.ELECTRONIC, 200.0)
        );

        OrderValuesCalculator calculator = new OrderValuesCalculator();

        System.out.println(String.format("Total Sum: %s", calculator.sumValue(orderList, allOrdersFilter)));

        System.out.println(String.format("Physical Sum: %s", calculator.sumValue(orderList, physicalOrdersFilter)));

        System.out.println(String.format("Electronic Sum: %s", calculator.sumValue(orderList, electronicOrderFilter)));
    }
}

enum OrderType { PHYSICAL, ELECTRONIC; }

@AllArgsConstructor
@Getter
class Order {
    private OrderType type;
    private Double value;
}

class OrderValuesCalculator {
    public Double sumValue(List<Order> orderList, Predicate<Order> filter){
        return orderList.stream()
                .filter(filter)
                .mapToDouble(Order::getValue).sum();
    }
}