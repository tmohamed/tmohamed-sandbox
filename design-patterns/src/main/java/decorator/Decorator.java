package decorator;

import java.math.BigDecimal;

public class Decorator {
    public static void main(String[] args) {
        AddonDecorator chocolateWithCaramelDecorator =
                new ChocolateDecorator(new CaramelDecorator(new Coffee()));

        AddonDecorator doubleCaramelDecorator =
                new CaramelDecorator(new CaramelDecorator(new Coffee()));

        AddonDecorator mintDecorator = new MintDecorator(new Tea());

        System.out.println(chocolateWithCaramelDecorator.calculateCost());
        System.out.println(doubleCaramelDecorator.calculateCost());
        System.out.println(mintDecorator.calculateCost());
    }
}

interface Beverage {
    BigDecimal calculateCost();
}

class Coffee implements Beverage {

    @Override
    public BigDecimal calculateCost() {
        return new BigDecimal("5.00");
    }
}

class Tea implements Beverage {

    @Override
    public BigDecimal calculateCost() {
        return new BigDecimal("3.00");
    }
}

abstract class AddonDecorator implements Beverage {
    protected Beverage beverage;

    public AddonDecorator(Beverage beverage){
        this.beverage = beverage;
    }
}

class CaramelDecorator extends AddonDecorator {
    public CaramelDecorator(Beverage beverage){
        super(beverage);
    }

    @Override
    public BigDecimal calculateCost() {
        return this.beverage.calculateCost().add(new BigDecimal("1.5"));
    }
}

class ChocolateDecorator extends AddonDecorator {
    public ChocolateDecorator(Beverage beverage){
        super(beverage);
    }

    @Override
    public BigDecimal calculateCost() {
        return this.beverage.calculateCost().add(new BigDecimal("2.5"));
    }
}

class MintDecorator extends AddonDecorator {
    public MintDecorator(Beverage beverage){
        super(beverage);
    }

    @Override
    public BigDecimal calculateCost() {
        return this.beverage.calculateCost().add(new BigDecimal("1.0"));
    }
}