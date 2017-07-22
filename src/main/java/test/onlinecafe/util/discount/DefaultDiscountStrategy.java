package test.onlinecafe.util.discount;

public class DefaultDiscountStrategy implements DiscountStrategy {
    // n'th cup of one type if free
    private int n = 5;
    // if order total > x then delivery is free
    private double x = 10;
    // delivery cost
    private double m = 2;

    public DefaultDiscountStrategy() {
    }

    public DefaultDiscountStrategy(int n, double x, int m) {
        this.n = n;
        this.x = x;
        this.m = m;
    }

    @Override
    public double getDiscountedItemCost(int quantity, double price) {
        return (quantity - quantity / 5) * price;
    }

    @Override
    public double getDeliveryCost(double orderTotal) {
        return orderTotal > x ? 0 : m;
    }
}
