package test.onlinecafe.util.discount;

public interface DiscountStrategy {
    void initFromConfigurationRepository();
    double getDiscountedItemCost(int quantity, double price);
    double getDeliveryCost(double orderTotal);
}
