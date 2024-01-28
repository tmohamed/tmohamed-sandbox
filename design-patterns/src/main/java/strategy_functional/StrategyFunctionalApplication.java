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
        calculator.setFilter(allOrdersFilter);
        System.out.println(String.format("Total Sum: %s", calculator.sumValue(orderList)));

        calculator.setFilter(physicalOrdersFilter);
        System.out.println(String.format("Physical Sum: %s", calculator.sumValue(orderList)));

        calculator.setFilter(electronicOrderFilter);
        System.out.println(String.format("Electronic Sum: %s", calculator.sumValue(orderList)));
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
    private Predicate<Order> filter;

    public void setFilter(Predicate<Order> filter){
        this.filter = filter;
    }

    public Double sumValue(List<Order> orderList){
        return orderList.stream()
                .filter(this.filter)
                .mapToDouble(Order::getValue).sum();
    }
}