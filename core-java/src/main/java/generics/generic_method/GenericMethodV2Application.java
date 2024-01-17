package generics.generic_method;

import java.util.List;

public class GenericMethodV2Application {
    public static void main(String[] args) {
        ListBox<String> stringListBox = new ListBox<>(List.of("O1", "O2", "O3"));
        stringListBox.getOrderList().forEach(System.out::println);

        System.out.println("------------------");

        ListBox<Integer> integerListBox = new ListBox<>(List.of(1, 2, 3));
        integerListBox.getOrderList().forEach(System.out::println);
    }
}

class ListBox<T> {
    private List<T> orderList;

    public ListBox(List<T> orderList){
        this.orderList = orderList;
    }

    public List<T> getOrderList() {
        return orderList;
    }
}
