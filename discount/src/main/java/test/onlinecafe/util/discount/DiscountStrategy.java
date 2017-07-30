package test.onlinecafe.util.discount;

public interface DiscountStrategy {
    void init();
    double getDiscountedItemCost(int quantity, double price);
    double getDeliveryCost(double orderTotal);
}
