package strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class StrategyApplication {
    public static void main(String[] args) {

        Filter allOrdersFilter = new AllOrderTypesFilter();
        Filter physicalOrdersFilter = new PhysicalOrderFilter();
        Filter electronicOrderFilter = new ElectronicOrderFilter();

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

interface Filter {
    List<Order> filter(List<Order> orderList);
}

class AllOrderTypesFilter implements Filter {
    @Override
    public List<Order> filter(List<Order> orderList) {
        return orderList;
    }
}

class PhysicalOrderFilter implements Filter {
    @Override
    public List<Order> filter(List<Order> orderList) {
        List<Order> filteredOrderList = new ArrayList<>();
        for(Order order : orderList)
        {
            if(order.getType().equals(OrderType.PHYSICAL))
            {
                filteredOrderList.add(order);
            }
        }
        return filteredOrderList;
    }
}

class ElectronicOrderFilter implements Filter {
    @Override
    public List<Order> filter(List<Order> orderList) {
        List<Order> filteredOrderList = new ArrayList<>();
        for(Order order : orderList)
        {
            if(order.getType().equals(OrderType.ELECTRONIC))
            {
                filteredOrderList.add(order);
            }
        }
        return filteredOrderList;
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
    public Double sumValue(List<Order> orderList, Filter filter){
        List<Order> orders = filter.filter(orderList);
        return orders.stream().mapToDouble(Order::getValue).sum();
    }
}