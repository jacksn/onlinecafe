package test.onlinecafe.util.discount;

public interface DiscountStrategy {
    double getDiscountedItemCost(int quantity, double price);
    double getDeliveryCost(double orderTotal);
}
